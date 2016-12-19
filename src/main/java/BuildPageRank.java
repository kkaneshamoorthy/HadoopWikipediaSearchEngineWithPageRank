import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class BuildPageRank {

    public static void runJob(String input, String output) throws Exception {

        Configuration conf = new Configuration();
        conf.set(XmlInputFormat.START_TAG_KEY, "<page>");
        conf.set(XmlInputFormat.END_TAG_KEY, "</page>");

        Job job = new Job(conf);
        job.setInputFormatClass(XmlInputFormat.class);
        job.setJarByClass(BuildPageRank.class);
        job.setMapperClass(PageRankMapper.class);
        job.setReducerClass(PageRankReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(TextTextPair.class);
        job.setNumReduceTasks(2);
        FileInputFormat.setInputPaths(job, input);

        Path outputPath = new Path(output);
        FileOutputFormat.setOutputPath(job, outputPath);
        outputPath.getFileSystem(conf).delete(outputPath, true);
        job.waitForCompletion(true);
    }

    public static void main(String[] args) throws Exception {
        runJob(args[0], args[args.length-1]);
    }
}

