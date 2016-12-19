package Graph;

public class PageNode {
    private  String id;
    private final String title;
    private  String body;
    private boolean defined;
    private double pageRankValue = 1;
    private boolean pageRankCalculated;

    public PageNode(String title) {
        this.id = null;
        this.title = title;
        this.body = null;
        this.defined = false;
    }

    public PageNode(String id, String title, String body) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.defined = true;
    }

    public void define(String id, String body) {
        if(defined) System.err.println("Already defined: " + this.title);
        this.id = id;
        this.body = body;
        defined = true;
    }

    public void setPageRankValueCalculated(boolean val) {
        this.pageRankCalculated = val;
    }

    public void setPageRankValue(double val) {
        this.pageRankValue = val;
    }

    public String getId() {return id; }
    public String getTitle() {return title; }
    public String getBody() {return body; }
    public boolean isDefined() {return defined; }
    public double getPageRankValue() {
        return pageRankValue;
    }
}
