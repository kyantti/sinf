package es.unex.cum.sinf.practica2;

import es.unex.cum.sinf.practica2.mappers.IncomeByYearMapper;
import es.unex.cum.sinf.practica2.reducers.IncomeByYearReducer;
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

import java.io.IOException;

public class Driver extends Configured implements Tool{
    @Override
    public int run(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Driver required params: {input file} {output dir}");
            System.exit(-1);
        }

        deleteOutputFileIfExists(args);

        final Job job = new Job(getConf());
        job.setJarByClass(Driver.class);
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        //Exercise 1
        job.setMapperClass(IncomeByYearMapper.class);
        job.setReducerClass(IncomeByYearReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(DoubleWritable.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.waitForCompletion(true);

        return 0;
    }
    private void deleteOutputFileIfExists(String[] args) throws IOException {
        final Path output = new Path(args[1]);
        FileSystem.get(output.toUri(), getConf()).delete(output, true);
    }
    public static void main(String[] args) throws Exception {
        ToolRunner.run(new Driver(), args);
    }
}
