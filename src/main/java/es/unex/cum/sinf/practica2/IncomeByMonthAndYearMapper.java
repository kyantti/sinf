package es.unex.cum.sinf.practica2;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IncomeByMonthAndYearMapper extends Mapper <Object, Text, Text, DoubleWritable>{
    private static final String SEPARATOR = ",";
    private static final int QUANTITY_ORDERED_INDEX = 2;
    private static final int PRICE_EACH_INDEX = 3;
    private static final int ORDER_DATE_INDEX = 4;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy HH:mm");
    private static final Logger logger = Logger.getLogger(IncomeByMonthAndYearMapper.class.getName());

    @Override
    public void map(Object key, Text value, Context context) throws InterruptedException, IOException {
        String[] columns = value.toString().split(SEPARATOR);
        if (hasValidColumns(columns)) {
            int quantityOrdered = parseQuantityOrdered(columns[QUANTITY_ORDERED_INDEX]);
            double priceEach = parsePriceEach(columns[PRICE_EACH_INDEX]);
            String monthAndYear = parseMonthAndYear(columns[ORDER_DATE_INDEX]);
            if (quantityOrdered > 0 && priceEach >= 0 && !monthAndYear.isEmpty()) {
                context.write(new Text(monthAndYear), new DoubleWritable(quantityOrdered * priceEach));
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
            logger.log(Level.WARNING, "Invalid quantity format: " + text, e);
        }
        return quantityOrdered;
    }

    private double parsePriceEach(String text) {
        double price = -1;
        try {
            double parsedPrice = Double.parseDouble(text);
            if (parsedPrice > 0) {
                price = parsedPrice;
            }
        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, "Invalid price format: " + text, e);
        }
        return price;
    }

    private String parseMonthAndYear(String text) {
        // If the date is valid (MM/dd/yy HH:mm) return the month and year (MM/yy) as a String
        // If the date is not valid return empty String
        String monthAndYear = "";
        try {
            if (dateFormat.parse(text) != null) {
                monthAndYear = text.substring(0, 5);
            }
        } catch (ParseException e) {
            logger.log(Level.WARNING, "Invalid date format: " + text, e);
        }
        return monthAndYear;
    }
}
