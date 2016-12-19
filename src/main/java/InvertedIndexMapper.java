import Parser.WikiPageParser;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.util.regex.Pattern;

import java.io.IOException;

public class InvertedIndexMapper extends Mapper<LongWritable, Text, Text, TextIntPair> {
    //Emits: <word> <docID, freq>>
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        try {
            WikiPageParser parser = new WikiPageParser(value.toString());
            String textContent = parser.getBody();

            Pattern p = Pattern.compile("[^a-zA-Z]");

            textContent = textContent.replace("''", "");
            textContent = textContent.replace(",", "");
            textContent = textContent.replace(".", "");
            textContent = textContent.replace("-", " ");
            textContent = textContent.replace("'", "");
            textContent = textContent.replace("[[", "");
            textContent = textContent.replace("]]", "");
            textContent = textContent.replace("\\|", "");
            textContent = textContent.replace("\\/", "");
            textContent = textContent.replace("\\>", "");
            textContent = textContent.replace("\\<", "");
            textContent = textContent.replace("}", "");
            textContent = textContent.replace("{", "");

            String[] body = textContent.split(" ");
            String title = parser.getTitle();
            String id = parser.getId();
            Text docID = new Text(id);


            for (String word : body) {
                if(word.trim().length() > 0) {
                    if(!p.matcher(word).find()) {
                        context.write(new Text(word.toLowerCase()), this.generateDocIDFreqPair(docID.toString(), 1));
                    }
                }
            }
        } catch (Exception e) {

        }
    }

    private TextIntPair generateDocIDFreqPair(String docID, int freq) {
        TextIntPair wordWithFreq = new TextIntPair(docID, freq);

        return wordWithFreq;
    }
}
