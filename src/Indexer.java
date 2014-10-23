import TermStorage.BTreeTermStorage;
import TermStorage.HashMapTermStorage;
import TermStorage.StorageInterface;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import foo.Term;
import org.tartarus.snowball.SnowballStemmer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Indexer {

    public static final String LIBRARY = "org.tartarus.snowball.ext.englishStemmer";
    StorageInterface terms = new BTreeTermStorage();
    int fileId = 1;
    SnowballStemmer stemmer;


    public Indexer() {
        try {
            Class stemClass = Class.forName(LIBRARY);
            stemmer = (SnowballStemmer) stemClass.newInstance();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public void parseFolder(String path) {
        try {
            Files.walk(Paths.get(path)).forEach(filePath -> {
                if (Files.isRegularFile(filePath)) {
                    File file = new File(filePath.toString());
                    int position = 0;

                    PTBTokenizer ptbt = null;
                    try {
                        ptbt = new PTBTokenizer(
                                new FileReader(filePath.toString()),
                                new CoreLabelTokenFactory(),
                                "untokenizable=noneKeep"
                        );


                        for (CoreLabel label; ptbt.hasNext(); ) {
                            label = (CoreLabel) ptbt.next();
                            String term = label.current();
                            term = removeSiblings(term);
                            term = stemming(term);

                            System.out.println(term);

                            position++;

                            terms.addTerm(term, fileId, filePath.toString(), position);

                        }

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                fileId++;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String removeSiblings(String nextToken) {
        return nextToken.replaceAll("[^a-zA-Z]", "").toLowerCase();
    }

    private String stemming(String token) {
        stemmer.setCurrent(token);
        stemmer.stem();
        return stemmer.getCurrent();
    }

    /*public Collection<Integer> intersect(Set<Integer> s1, Set<Integer> s2) {
        s1.retainAll(s2);
        return s1;
    }*/
}
