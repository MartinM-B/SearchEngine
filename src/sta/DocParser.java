package sta;

import java.io.IOException;
import java.util.Set;

public class DocParser {

    public static void main(String[] args) throws IOException {
        Indexer i = new Indexer();
        i.parseFolder("docs");
        Set mergeResult = i.merge(i.merge(i.merge("authors", "in"), "home"), "me");
        
        String [] terms = {
                "you", "me", "home", "in"
        };
        Set mergeResult2 = i.merge(terms);


        Set orResults = i.or("authors", "me");


        Set orResults1 = i.or(i.or(i.or("authors", "Garamond"), "home"), "me");

        String [] terms2 = {
                "authors", "Garamond", "home", "me"
        };
        Set orResult2 = i.or(terms2);


        Set notResult = i.not("Garamond");


        Set allResult = i.merge(i.not("Garamond"), "home");
        Set allResult1 = i.merge(i.or(i.not("Garamond"), "me"), "home");

        System.out.print("Bla");
    }
}
