package sta;

import java.io.IOException;
import java.util.Set;

public class DocParser {

    public static void main(String[] args) throws IOException {
        Indexer i = new Indexer();
        i.parseFolder("docs");

        System.out.println("Standard Dictionary: " + i.createDictionaryString(0));
        System.out.println("Blocked Dictionary: " + i.createDictionaryString(1));
        System.out.println("FrontCoded Dictionary: " + i.createDictionaryString(2));



        Set mergeResult = i.merge(i.merge(i.merge("authors", "in"), "home"), "me");
        
        String [] terms = {
                "you", "me", "home", "in"
        };
        Set mergeResult2 = i.merge(terms);

        System.out.println(i.searchWithWildcard("holiday"));
        System.out.println(i.searchWithWildcard("you"));
        System.out.println(i.searchWithWildcard("a$"));

        Set orResults = i.or("authors", "me");


        Set orResults1 = i.or(i.or(i.or("authors", "Garamond"), "home"), "me");

        String [] terms2 = {
                "authors", "Garamond", "home", "me"
        };
        Set orResult2 = i.or(terms2);


        Set notResult = i.not("Garamond");


        Set allResult = i.merge(i.not("Garamond"), "home");
        Set allResult1 = i.merge(i.or(i.not("Garamond"), "me"), "home");
        Set allResult123123123 = i.merge(i.or(i.not("asdg"), "me"), "home");

    }
}
