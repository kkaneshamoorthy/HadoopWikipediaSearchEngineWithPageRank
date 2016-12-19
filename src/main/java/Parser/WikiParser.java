package Parser;


import Graph.LinkGraph;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;

/**
 * Created by mb on 14/12/2016.
 */
public class WikiParser {
    File input;
    SAXParser parser;

    public WikiParser(String filename) throws ParserConfigurationException, SAXException {
        input = new File(getClass().getResource(filename).getFile());
        parser = SAXParserFactory.newInstance().newSAXParser();
    }

    public WikiParser() throws ParserConfigurationException, SAXException {
        parser = SAXParserFactory.newInstance().newSAXParser();
    }

    public String getBody(String page) throws ParserConfigurationException, SAXException, IOException {

        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        WikiHandler handler = new WikiHandler();
        saxParser.parse(new InputSource(new StringReader(page)), handler);

        return handler.getBody().toString();
    }


    public LinkGraph buildGraph() throws IOException, SAXException {
        WikiHandler handler = new WikiHandler();
        parser.parse(input, handler);
        return handler.getGraph();
    }
}
