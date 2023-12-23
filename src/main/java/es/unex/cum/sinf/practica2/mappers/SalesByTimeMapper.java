package es.unex.cum.sinf.practica2.mappers;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SalesByTimeMapper extends Mapper<Object, Text, Text, IntWritable> {
    private static final String SEPARATOR = ",";
    private static final int QUANTITY_ORDERED_INDEX = 2;
    private static final int ORDER_DATE_INDEX = 4;
    private static final SimpleDateFormat inputDateFormat = new SimpleDateFormat("MM/dd/yy HH:mm");
    private static final SimpleDateFormat outputDateFormat = new SimpleDateFormat("HH");
    private static final Logger logger = Logger.getLogger(SalesByTimeMapper.class.getName());

    @Override
    public void map(Object key, Text value, Context context) throws InterruptedException, IOException {
        String[] columns = value.toString().split(SEPARATOR);
        if (hasValidColumns(columns)) {
            int quantityOrdered = parseQuantityOrdered(columns[QUANTITY_ORDERED_INDEX]);
            String time = parseTime(columns[ORDER_DATE_INDEX]);
            if (quantityOrdered > 0 && !time.isEmpty()) {
                context.write(new Text(time), new IntWritable(quantityOrdered));
            }
        }
    }

    private boolean hasValidColumns(String[] columns) {
        return columns.length >= Math.max(ORDER_DATE_INDEX, QUANTITY_ORDERED_INDEX) + 1 &&
                !columns[ORDER_DATE_INDEX].isEmpty() &&
                !columns[QUANTITY_ORDERED_INDEX].isEmpty();
    }

    private int parseQuantityOrdered(String text) {
        int quantityOrdered = -1;
        try {
            int parsedQuantity = Integer.parseInt(text);
            if (parsedQuantity > 0) {
                quantityOrdered = parsedQuantity;
            }
        } catch (NumberFormatException e) {
            logger.log(Level.SEVERE, "Invalid quantity format: " + text, e);
        }
        return quantityOrdered;
    }

    private String parseTime(String text) {
        String time = "";
        try {
            inputDateFormat.setLenient(false);
            time = outputDateFormat.format(inputDateFormat.parse(text));
        } catch (ParseException e) {
            logger.log(Level.SEVERE, "Invalid date format: " + text, e);
        }
        return time;
    }
}
