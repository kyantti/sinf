package es.unex.cum.sinf.practica2.reducers;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;

import java.io.IOException;
import java.text.DecimalFormat;

public class GenericReducer<K extends Writable, V extends Writable> extends Reducer<Text, V, Text, Text> {
    private final Text outputValue = new Text();
    private final DecimalFormat decimalFormat = new DecimalFormat("#.##");
    @Override
    public void reduce(Text key, Iterable<V> values, Context context) throws IOException, InterruptedException {
        double total = 0;

        for (V value : values) {
            if (value instanceof DoubleWritable) {
                total += ((DoubleWritable) value).get();
            }
            else if (value instanceof IntWritable) {
                total += ((IntWritable) value).get();
            }
        }

        if (total % 1 == 0) {
            outputValue.set(String.format("%.0f", total));
        } else {
            outputValue.set(decimalFormat.format(total));
        }

        context.write(key, outputValue);
    }
}
