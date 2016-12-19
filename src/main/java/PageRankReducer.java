import java.io.IOException;
import java.util.ArrayList;

import Graph.HadoopLinkGraph;
import Graph.HadoopPageNode;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
//key: docID, Value: word/link, freq
public class PageRankReducer extends Reducer<Text, TextTextPair, Text, Text> {
    ArrayList<HadoopPageNode> links = new ArrayList<HadoopPageNode>();
    public void reduce(Text key, Iterable<TextTextPair> values, Context context) throws IOException, InterruptedException {
        String docID = key.toString();
        HadoopPageNode page = new HadoopPageNode(docID, values.iterator().next().getSecond().toString());

        for (TextTextPair wordWithFreq : values) {
            String link = wordWithFreq.getFirst().toString();
            HadoopPageNode linksPage = new HadoopPageNode(link);
            linksPage.setBody(link);
            links.add(linksPage);
        }

        HadoopLinkGraph graph = new HadoopLinkGraph();
        graph.addNode(page, links);
        graph.calculatePageRank();
        ArrayList<HadoopPageNode> definedNodes = graph.getAllDefinedNodes();

        for (HadoopPageNode hadoopPageNode : definedNodes) {
            context.write(new Text(hadoopPageNode.getId()), new Text(hadoopPageNode.getTitle() + " " + hadoopPageNode.getPageRankValue()));
        }
    }
}
