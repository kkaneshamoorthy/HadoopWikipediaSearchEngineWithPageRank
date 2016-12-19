import Graph.HadoopPageNode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

public class WikipediaSearchEngine {

    private HashMap<String, HadoopPageNode> titlePageMap;
    private HashMap<String, HadoopPageNode> idPageMap;
    private HashMap<String, HashMap<String, Integer>> wordDocIdFreqMap;

    private final String NO_PAGE_FOUND_MESSAGE = "There were no results matching the query";

    public WikipediaSearchEngine() {
        this.titlePageMap = new HashMap<String, HadoopPageNode>();
        this.idPageMap = new HashMap<String, HadoopPageNode>();
        this.wordDocIdFreqMap = new HashMap<String, HashMap<String, Integer>>();

        try {
            this.setUp();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void search(String searchTerm) {
        ArrayList<HadoopPageNode> pagesThatMatches = new ArrayList<HadoopPageNode>();

        for (String term : searchTerm.toLowerCase().trim().split(" ")) {
            pagesThatMatches.addAll(this.searchSubTerm(term));

            for (String word : wordDocIdFreqMap.keySet()) {
                if (word.contains(term)) {
                    Iterator<String> ite = wordDocIdFreqMap.get(word).keySet().iterator();
                    String docID = ite.next();
                    String title = this.getTitle(docID);
                    HadoopPageNode page = new HadoopPageNode(docID, title);
                    page.setPageRankValue(this.getPageRank(docID));
                    pagesThatMatches.add(page);
                }
            }
        }

        printSearchResults(this.getTopTenPages(pagesThatMatches));
    }

    private void printSearchResults(Set<String> topTenPages) {
        if (topTenPages.size() == 0) {
            System.out.println(this.NO_PAGE_FOUND_MESSAGE);
            return;
        }

        System.out.println("---Search result---");
        int counter = 1;
        for (String title : topTenPages) {
            System.out.println(counter + " - " + title);
            counter++;
        }
        System.out.println("---End of result---");
    }

    private ArrayList<HadoopPageNode> searchSubTerm(String subSearchTerm) {
        ArrayList<HadoopPageNode> matchedPages = new ArrayList<HadoopPageNode>();
        for (String title : this.titlePageMap.keySet())
            if (title.contains(subSearchTerm))
                matchedPages.add(this.titlePageMap.get(title));

        return matchedPages;
    }

    private Set<String> getTopTenPages(ArrayList<HadoopPageNode> matchedPages) {
        Set<String> topTenPages = new HashSet<String>();
        sort(matchedPages);
        int counter = 0;
        for (HadoopPageNode page : matchedPages) {
            if (counter <= 9) {
                topTenPages.add(page.getTitle());
            } else
                break;
            counter++;
        }

        return topTenPages;
    }

    private void setUp() throws IOException, URISyntaxException {
        File pageRankFile = new File(this.getClass().getResource("/pageRankOutput.txt").toURI());
        BufferedReader br = new BufferedReader(new FileReader(pageRankFile));
        String line = "";
        while ((line = br.readLine()) != null) {
            String[] data = line.split(";");
            String docID = data[0].trim();
            String title = data[1].trim();
            String pr = data[2].trim();
            Double pageRankValue = Double.parseDouble(pr);

            HadoopPageNode page = new HadoopPageNode(docID, title);
            page.setPageRankValue(pageRankValue);

            this.titlePageMap.put(title, page);
            this.idPageMap.put(docID, page);

        }
        br.close();

        this.readInInvertedIndexResult("/part-r-00000.txt" );
        this.readInInvertedIndexResult("/part-r-00001.txt" );


    }

    private void readInInvertedIndexResult(String file) throws IOException, URISyntaxException {
        File invertedIndexFile = new File(this.getClass().getResource(file).toURI());
        BufferedReader brInvertedIndex = new BufferedReader(new FileReader(invertedIndexFile));
        String line = "";
        while ((line = brInvertedIndex.readLine()) != null) {
            String[] data = line.split(";");
            String word = data[0].trim();
            String docID = data[1].trim();
            int freq = Integer.parseInt(data[2].trim());

            if (this.wordDocIdFreqMap.containsKey(word)) {
                HashMap<String, Integer> docFreqMap =this.wordDocIdFreqMap.get(word);
                docFreqMap.put(docID, freq);
            } else {
                HashMap<String, Integer> docFreqMap = new HashMap<String, Integer>();
                docFreqMap.put(docID, freq);
                this.wordDocIdFreqMap.put(word, docFreqMap);
            }
        }

        brInvertedIndex.close();
    }

    private String getTitle(String docID) {
        if (this.idPageMap.containsKey(docID)) {
            return this.idPageMap.get(docID).getTitle();
        }

        return "Title not found";
    }

    private double getPageRank(String docID) {
        if (this.idPageMap.containsKey(docID)) {
            return this.idPageMap.get(docID).getPageRankValue();
        }

        return -1;
    }

    public void sort(ArrayList<HadoopPageNode> matchedPages) {
        Collections.sort(matchedPages);
    }
}
