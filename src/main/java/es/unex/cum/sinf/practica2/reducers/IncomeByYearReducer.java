package es.unex.cum.sinf.practica2.reducers;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.text.DecimalFormat;

public class IncomeByYearReducer extends Reducer<Text, DoubleWritable, Text, Text> {
    private final DecimalFormat decimalFormat = new DecimalFormat("#.##");
    public void reduce(Text key, Iterable<DoubleWritable> values, Context context) throws IOException, InterruptedException {
        int totalIncome = 0;

        for (DoubleWritable value : values) {
            totalIncome += value.get();
        }

        context.write(key, new Text(decimalFormat.format(totalIncome)));
    }
}
