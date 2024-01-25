package es.unex.cum.sinf.practica2.reducers;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MostSoldProductByCityReducer <K extends Writable, V extends Writable> extends Reducer<Text, V, Text, Text> {
    private final Text outputValue = new Text();
    private static final Logger logger = Logger.getLogger(MostSoldProductByCityReducer.class.getName());
    @Override
    protected void reduce(Text key, Iterable<V> values, Context context) throws IOException, InterruptedException {
        // Key format: "city,product"
        String[] keyParts = key.toString().split(";");
        if (keyParts.length != 2) {
            logger.log(Level.WARNING, "Invalid key format: " + key.toString());
            return;
        }

        String city = keyParts[0];
        String mostSoldProduct = "";
        int maxQuantity = Integer.MIN_VALUE;

        // Iterate through values to find the highest total quantity and the corresponding product
        for (V value : values) {
            int quantity = ((IntWritable) value).get();
            if (quantity > maxQuantity) {
                maxQuantity = quantity;
                mostSoldProduct = keyParts[1]; // Update mostSoldProduct with the current product
            }
        }

        if (mostSoldProduct != null) {
            // Emit the result with the city as key and the most sold product as value
            outputValue.set(mostSoldProduct);
            context.write(new Text(city), outputValue);
        }
    }
}
