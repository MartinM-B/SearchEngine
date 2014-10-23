import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import org.tartarus.snowball.SnowballStemmer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Indexer {

    public static final String LIBRARY = "org.tartarus.snowball.ext.englishStemmer";

    TermStorage terms = new TermStorage();
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

                            position++;
                            if (!term.equals("")) {
                                if (!terms.contains(term)) {
                                    Term t = new Term(term);
                                    terms.addTerm(t);
                                }
                                terms.getTerm(term).addPosition(fileId, filePath.toString(), position);
                            }
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


    public Set merge(String termName1, String termName2) {

        Set<Integer> positions = this.terms.getTerm(termName1).getDocumentIds();
        Set<Integer> positions1 = this.terms.getTerm(termName2).getDocumentIds();

        positions.retainAll(positions1);
        return positions;

    }


    public TermStorage getTerms() {
        return terms;
    }
}
