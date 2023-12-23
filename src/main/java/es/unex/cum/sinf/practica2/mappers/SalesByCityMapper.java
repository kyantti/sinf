package es.unex.cum.sinf.practica2.mappers;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SalesByCityMapper extends Mapper<Object, Text, Text, IntWritable> {
    private static final String SEPARATOR = ",";
    private static final int QUANTITY_ORDERED_INDEX = 2;
    private static final int PURCHASE_ADDRESS_INDEX = 5;
    private static final Logger logger = Logger.getLogger(SalesByCityMapper.class.getName());
    @Override
    public void map(Object key, Text value, Context context) throws InterruptedException, IOException {
        String[] columns = value.toString().split(SEPARATOR);
        if (hasValidColumns(columns)) {
            int quantityOrdered = parseQuantityOrdered(columns[QUANTITY_ORDERED_INDEX]);
            String cityAndState = extractCityAndState(columns[PURCHASE_ADDRESS_INDEX]);
            if (quantityOrdered > 0) {
                context.write(new Text(cityAndState), new IntWritable(quantityOrdered));
            }
        }
    }
    private boolean hasValidColumns(String[] columns) {
        return columns.length >= Math.max(QUANTITY_ORDERED_INDEX, PURCHASE_ADDRESS_INDEX) + 1 &&
                !columns[QUANTITY_ORDERED_INDEX].isEmpty() &&
                !columns[PURCHASE_ADDRESS_INDEX].isEmpty();
    }

    private int parseQuantityOrdered(String text) {
        int quantityOrdered = -1;
        try {
            int parsedQuantity = Integer.parseInt(text);
            if (parsedQuantity > 0) {
                quantityOrdered = parsedQuantity;
            }
        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, "Invalid quantity format: " + text, e);
        }
        return quantityOrdered;
    }

    private String extractCityAndState(String address) {
        // Example: 917 1st St, Dallas, TX 75001
        String[] addressParts = address.split(";");
        if (addressParts.length >= 3) {
            String city = addressParts[1].trim();
            String state = addressParts[2].trim().split(" ")[0];
            return city + ", " + state;
        }
        return "";
    }
}
