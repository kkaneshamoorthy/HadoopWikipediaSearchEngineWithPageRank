package Graph;

public class HadoopPageNode implements Comparable<HadoopPageNode>{

    private String id;
    private String title;
    private boolean defined = false;
    private double pageRankValue = 1;
    private boolean pageRankCalculated;
    private String body;


    public HadoopPageNode(String title) {
        this.title = title;
        this.id = null;
        this.defined = false;
        this.body = null;
    }

    public HadoopPageNode(String id, String title) {
        this.id = id;
        this.title = title;
        this.defined = true;
        this.body = null;
    }

    public void define(String id) {
        this.id = id;
        this.defined = true;
    }

    public void setBody(String word) { this.body = word; }
    public String getId() { return this.id; }
    public String getTitle() { return this.title; }
    public boolean isDefined() { return defined; }
    public void setPageRankValue(double val) {
        this.pageRankValue = val;
    }
    public double getPageRankValue() {
        return pageRankValue;
    }
    public String getBody() { return this.body; }

    public int compareTo(HadoopPageNode o) {
        if (pageRankValue > o.pageRankValue)
            return 1;
        else if (pageRankValue < o.pageRankValue)
            return -1;
        else
            return 0;
    }
}
