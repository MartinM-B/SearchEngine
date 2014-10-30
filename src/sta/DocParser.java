package sta;

import java.io.IOException;
import java.util.Set;

public class DocParser {

    public static void main(String[] args) throws IOException {
        Indexer i = new Indexer();
        i.parseFolder("docs");
        Set mergeResult = i.merge("you", "me");

      //  System.out.println(i.searchWithWildcard("holiday"));
      //  System.out.println(i.searchWithWildcard("you"));
        System.out.println(i.searchWithWildcard("A130"));
      //  System.out.println(i.searchWithWildcard("a$"));

    }
}
