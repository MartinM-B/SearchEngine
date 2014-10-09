import java.io.IOException;

public class DocParser {

    public static void main(String[] args) throws IOException {
        Indexer i = new Indexer();
        i.parseFolder("docs");


    }
}
