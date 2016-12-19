package Parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mb on 15/12/2016.
 [[link]] <-- will extract text from patterns containing double brackets
 */

public class LinkParser {

    Pattern linkPattern;
    Matcher m;

    public LinkParser() {
        linkPattern = Pattern.compile("\\[\\[([^\\]\\]]+)");
    }

    public ArrayList<String> parse(String text) {
        m = linkPattern.matcher(text);
        ArrayList<String> links = new ArrayList();

        while(m.find()) {
            String link = m.group(1);
            link = link.split("\\|")[0];
            if(link.matches("[a-zA-Z0-9 -]*")) {
                links.add(link);
            }
        }
        return links;
    }
}
