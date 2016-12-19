import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        WikipediaSearchEngine wse = new WikipediaSearchEngine();
        Scanner input = new Scanner(System.in);

        while(true) {
            System.out.print("Search: ");
            String searchTerm = input.nextLine();
            wse.search(searchTerm);
        }
    }
}
