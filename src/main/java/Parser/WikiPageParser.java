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

public class WikiPageParser {
    SAXParser parser;
    WikiHandler handler;

    public WikiPageParser(String page) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        parser = factory.newSAXParser();
        handler = new WikiHandler();
        parser.parse(new InputSource(new StringReader(page)), handler);
    }

    public String getBody() throws ParserConfigurationException, SAXException, IOException {
        return handler.getBody().toString();
    }

    public String getTitle() {
        return handler.getTitle();
    }

    public String getId() {
        return handler.getID();
    }
}
