package Graph;

import java.util.ArrayList;
import java.util.HashMap;

public class LinkGraph {

    private HashMap<PageNode, ArrayList<PageNode>> outGoingLinkMap = new HashMap(100000);
    private HashMap<PageNode, ArrayList<PageNode>> incomingLinkMap = new HashMap(100000);
    private HashMap<String, PageNode> titleMap = new HashMap(100000);
    private HashMap<String, PageNode> idMap = new HashMap(100000);
    private final double DAMPENING_FACTOR = 0.85;
    private final int ITERATIONS = 10;

    public LinkGraph() {}

        public void addNode(PageNode node, ArrayList<PageNode> links) {

        PageNode toBeAdded = null;

            if(titleMap.containsKey(node.getTitle())) {
                toBeAdded = titleMap.get(node.getTitle());
                if(!toBeAdded.isDefined()) {
                    toBeAdded.define(node.getId(), node.getBody());
                }
            } else {
                toBeAdded = node;
                titleMap.put(node.getTitle(), node);
                idMap.put(node.getId(), node);
                incomingLinkMap.put(node, new ArrayList<PageNode>());
            }

            for(int i = 0; i < links.size(); i++) {
                PageNode n = links.get(i);
                if(!titleMap.containsKey(n.getTitle())) {
                    titleMap.put(n.getTitle(), n);
                    incomingLinkMap.put(n, new ArrayList<PageNode>());
                } else {
                    links.set(i,titleMap.get(n.getTitle()));
                }

                n = links.get(i);

                incomingLinkMap.get(n).add(node);
            }



            outGoingLinkMap.put(toBeAdded, links);

    }

    public PageNode getNodeByTitle(String title) {
        return titleMap.get(title);
    }

    public static ArrayList<PageNode> createLinks(ArrayList<String> titles) {
        ArrayList<PageNode> links = new ArrayList();

        for(String title : titles) {
            PageNode undefinedNode = new PageNode(title);
            links.add(undefinedNode);
        }

        return links;
    }

    public ArrayList<PageNode> getOutgoingLinks(String title) {
        return outGoingLinkMap.get(getNodeByTitle(title));
    }

    public ArrayList<PageNode> getIncomingLinks(String title) {
        return incomingLinkMap.get(getNodeByTitle(title));
    }

    public ArrayList<PageNode> getOutgoingLinks(PageNode node) {
        return outGoingLinkMap.get(node);
    }

    public ArrayList<PageNode> getIncomingLinks(PageNode node) {
        return incomingLinkMap.get(node);
    }

    public void calculatePageRank() {

        for(int i = 0; i < ITERATIONS; i++) {
            for (String title : titleMap.keySet()) {
                PageNode n = titleMap.get(title);
                if(n.isDefined()) {
                    double sum = 0;
                    for (PageNode incomingLinkNode : this.getIncomingLinks(n)) {
                        sum += (n.getPageRankValue() * DAMPENING_FACTOR) / this.getOutgoingLinks(incomingLinkNode).size();
                    }
                    sum += (1 - DAMPENING_FACTOR);
                    n.setPageRankValue(sum);
                }
            }
        }
    }

    public ArrayList<PageNode> getAllDefinedNodes() {
        ArrayList<PageNode> nodes = new ArrayList();

        for(String title : titleMap.keySet()) {
            PageNode n = titleMap.get(title);

            if(n.isDefined()) {
                nodes.add(n);
            }
        }

        return nodes;
    }

    public PageNode getPageNodeById(String id) {
        return idMap.get(id);
    }

    public ArrayList<PageNode> getAllNodes() {
        return (ArrayList) titleMap.values();
    }

}
