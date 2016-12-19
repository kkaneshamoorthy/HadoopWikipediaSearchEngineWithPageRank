package Graph;

import java.util.ArrayList;
import java.util.HashMap;

public class HadoopLinkGraph {
    private HashMap<HadoopPageNode, ArrayList<HadoopPageNode>> outGoingLinkMap = new HashMap(100000);
    private HashMap<HadoopPageNode, ArrayList<HadoopPageNode>> incomingLinkMap = new HashMap(100000);
    private HashMap<String, HadoopPageNode> titleMap = new HashMap(100000);
    private HashMap<String, HadoopPageNode> idMap = new HashMap(100000);
    private final double DAMPENING_FACTOR = 0.85;
    private final int ITERATIONS = 10;

    public HadoopLinkGraph() {}

    public void addNode(HadoopPageNode node, ArrayList<HadoopPageNode> links){
        HadoopPageNode toBeAdded = null;

        if(titleMap.containsKey(node.getTitle())) {
            toBeAdded = titleMap.get(node.getTitle());
            if(!toBeAdded.isDefined()) {
                toBeAdded.define(node.getId());
            }
        } else {
            toBeAdded = node;
            titleMap.put(node.getTitle(), node);
            idMap.put(node.getId(), node);
            incomingLinkMap.put(node, new ArrayList<HadoopPageNode>());
        }

        for(int i = 0; i < links.size(); i++) {
            HadoopPageNode n = links.get(i);
            if(!titleMap.containsKey(n.getTitle())) {
                titleMap.put(n.getTitle(), n);
                incomingLinkMap.put(n, new ArrayList<HadoopPageNode>());
            } else {
                links.set(i,titleMap.get(n.getTitle()));
            }

            n = links.get(i);

            incomingLinkMap.get(n).add(node);
        }

        outGoingLinkMap.put(toBeAdded, links);
    }

    public ArrayList<HadoopPageNode> getOutgoingLinks(HadoopPageNode node) {
        return outGoingLinkMap.get(node);
    }

    public ArrayList<HadoopPageNode> getIncomingLinks(HadoopPageNode node) {
        return incomingLinkMap.get(node);
    }

    public void calculatePageRank() {
        for(int i = 0; i < ITERATIONS; i++) {
            for (String title : titleMap.keySet()) {
                HadoopPageNode n = titleMap.get(title);
                if(n.isDefined()) {
                    double sum = 0;
                    for (HadoopPageNode incomingLinkNode : this.getIncomingLinks(n)) {
                        sum += (n.getPageRankValue() * DAMPENING_FACTOR) / this.getOutgoingLinks(incomingLinkNode).size();
                    }
                    sum += (1 - DAMPENING_FACTOR);
                    n.setPageRankValue(sum);
                }
            }
        }
    }

    public ArrayList<HadoopPageNode> getAllDefinedNodes() {
        ArrayList<HadoopPageNode> nodes = new ArrayList();

        for(String title : titleMap.keySet()) {
            HadoopPageNode n = titleMap.get(title);

            if(n.isDefined()) {
                nodes.add(n);
            }
        }

        return nodes;
    }
}
