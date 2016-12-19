package Parser;

import Graph.LinkGraph;
import Graph.PageNode;
import org.apache.avro.generic.GenericData;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WikiHandler extends DefaultHandler {

    LinkGraph graph;
    boolean parsingComplete = false;
    LinkParser linkParser;
    String id = null;
    StringBuilder body;
    String title = null;
    ArrayList<PageNode> links;
    boolean onId = false;
    boolean onTitle = false;
    boolean onBody = false;
    boolean onRevision = false;

    public void startDocument() throws SAXException {
        System.out.println("Parsing has started");
        graph = new LinkGraph();
        linkParser = new LinkParser();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(qName.equals("id") && !onRevision) {
            onId = true;
        } else if(qName.equals("title")){
            onTitle = true;
        } else if(qName.equals("revision")) {
            onRevision = true;
        } else if(qName.equals("text")) {
            body = new StringBuilder();
            onBody = true;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {

        if(onId && !onRevision) {
            id = new String(ch, start, length).toLowerCase();
        } else if(onTitle){
            title = new String(ch, start, length).toLowerCase();
        } else if(onBody) {
            body.append(new String(ch, start, length).toLowerCase());
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(onId && !onRevision) {
            onId = false;
        } else if(onTitle){
             onTitle = false;
        } else if(onRevision) {
            onRevision = false;
        } else if(onBody) {
            onBody = false;
        }

       if(qName.equals("page")) {
           PageNode node = new PageNode(id, title, body.toString());
           parseLinks(body.toString());
           graph.addNode(node, links);
       }
    }

    public void parseLinks(String body) {
        ArrayList<String> linkTitles = linkParser.parse(body);
        links = LinkGraph.createLinks(linkTitles);
    }

    public void endDocument() throws SAXException {
        System.out.println("Parsing has ended");
        parsingComplete = true;
    }

    public LinkGraph getGraph() {
        if(parsingComplete) {
            return graph;
        }

        System.err.println("Parsing has not started");
        return null;
    }

    public StringBuilder getBody() {
        return this.body;
    }

    public String getTitle() {
        return this.title;
    }

    public String getID(){
        return this.id;
    }
}
