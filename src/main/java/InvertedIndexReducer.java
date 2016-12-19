import java.io.IOException;
import java.util.Map;

import Graph.HadoopPageNode;
import InvertedIndex.BuildInvertedIndexHelper;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class InvertedIndexReducer extends Reducer<Text, TextIntPair, Text, Text> {

    HadoopPageNode page = null;
    private IntWritable result = new IntWritable();
    BuildInvertedIndexHelper invertedIndex = new BuildInvertedIndexHelper();

    public void reduce(Text key, Iterable<TextIntPair> values, Context context) throws IOException, InterruptedException {
        String word = key.toString();

        for (TextIntPair wordWithFreq : values) {
            String docID = wordWithFreq.getFirst().toString();
            int freq = wordWithFreq.getSecond().get();

            invertedIndex.addWordsToInvertedIndex(word, docID, freq);
        }
        invertedIndex.sortMap();
        Map<String, Integer> docWithFreq = invertedIndex.getDocWithFreq(word);

        int counter = 0;
        for (String docID : docWithFreq.keySet()) {
            if (counter <= 9)
                context.write(new Text(word), new Text(docID + " " + docWithFreq.get(docID)));
            else
                break;
        }
    }
}


