package es.unex.cum.sinf.practica2;

import es.unex.cum.sinf.practica2.mappers.*;
import es.unex.cum.sinf.practica2.reducers.GenericReducer;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class Driver extends Configured implements Tool {
    @Override
    public int run(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: Driver <inputDir> <outputDir>");
            System.exit(-1);
        }

        final String inputDir = args[0];
        final String outputDir = args[1];

        // Exercise 1
        runJob(inputDir, outputDir, "output_exercise1", IncomeByYearMapper.class, GenericReducer.class, DoubleWritable.class);

        // Exercise 2
        runJob(inputDir, outputDir, "output_exercise2", IncomeByMonthAndYearMapper.class, GenericReducer.class, DoubleWritable.class);

        // Exercise 3
        runJob(inputDir, outputDir, "output_exercise3", SalesByCityMapper.class, GenericReducer.class, IntWritable.class);

        // Exercise 4
        runJob(inputDir, outputDir, "output_exercise4", SalesByProductMapper.class, GenericReducer.class, IntWritable.class);

        // Exercise 5
        runJob(inputDir, outputDir, "output_exercise5", SalesByTimeMapper.class, GenericReducer.class, IntWritable.class);

        return 0;
    }

    private void runJob(String inputDir, String outputDir, String outputFileName, Class<? extends Mapper> mapperClass, Class<? extends Reducer> reducerClass, Class<? extends Writable> outputValueClass) throws IOException, ClassNotFoundException, InterruptedException {
        // Delete output directory if it exists
        deleteOutputFileIfExists(outputDir + "/" + outputFileName);
        final Job job = new Job(getConf());
        job.setJarByClass(Driver.class);
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        job.setMapperClass(mapperClass);
        job.setReducerClass(reducerClass);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(outputValueClass); // Set the correct output value class
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.addInputPath(job, new Path(inputDir));
        FileOutputFormat.setOutputPath(job, new Path(outputDir + "/" + outputFileName));

        job.waitForCompletion(true);
    }

    private void deleteOutputFileIfExists(String outputDir) throws IOException {
        final Path output = new Path(outputDir);
        FileSystem.get(output.toUri(), getConf()).delete(output, true);
    }

    public static void main(String[] args) throws Exception {
        ToolRunner.run(new Driver(), args);
    }
}
