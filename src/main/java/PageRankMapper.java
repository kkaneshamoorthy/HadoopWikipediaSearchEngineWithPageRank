import Parser.WikiPageParser;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class PageRankMapper extends Mapper<LongWritable, Text, Text, TextTextPair> {
    //Emits: <docID> <word, freq>>
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        try {
            WikiPageParser parser = new WikiPageParser(value.toString());
            String textContent = parser.getBody();
            String title = parser.getTitle();
            Text docID = new Text(parser.getId());
            String[] body = textContent.split(" ");

            for (int i=0; i<body.length; i++) {
                String word = body[i];
                if (word.contains("[[")) {
                    context.write(docID, this.generateTextIntPair(word, title));

                    i++;
                    String nextWord = body[i];
                    while (!nextWord.contains("]]")) {
                        context.write(docID, this.generateTextIntPair(nextWord, title));
                        i++;

                        if (i >= body.length - 1) break;
                        nextWord = body[i];
                    }
                }
            }
        } catch (Exception err) { }
    }

    private TextTextPair generateTextIntPair(String word, String title) {
        TextTextPair wordWithFreq = new TextTextPair(word, title);

        return wordWithFreq;
    }
}
