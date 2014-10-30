package sta;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import libs.snowball.SnowballStemmer;
import sta.entity.Term;
import sta.entity.TermStorageBTree;
import sta.entity.interfaces.StorageInterface;
import sta.utils.Soundex;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Indexer {

    public static final String LIBRARY = "libs.snowball.ext.englishStemmer";

    StorageInterface terms = new TermStorageBTree();

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

    @SuppressWarnings("unchecked")
    public void parseFolder(String path) {
        try {
            Files.walk(Paths.get(path)).forEach(filePath -> {
                if (Files.isRegularFile(filePath)) {

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

                            if(term.length() > 0) {
                                String termSoundex = soundex(term);
                                terms.addTerm(term, termSoundex, fileId, filePath.toString(), position);
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

    /**
     * @param term
     * @return
     */
    private String soundex(String term) {
        return Soundex.soundex(term);
    }

    private String removeSiblings(String nextToken) {
        return nextToken.replaceAll("[^a-zA-Z]", "").toLowerCase();
    }

    private String stemming(String token) {
        stemmer.setCurrent(token);
        stemmer.stem();
        return stemmer.getCurrent();
    }


    /**
     * @param termName1
     * @param termName2
     * @return
     */
    public Set merge(String termName1, String termName2) {
        Set<Integer> positions = this.terms.getTerm(termName1).getDocumentIds();
        Set<Integer> positions1 = this.terms.getTerm(termName2).getDocumentIds();

        positions.retainAll(positions1);
        return positions;
    }

    public Collection<Term> searchWithWildcard(String query) {
        return terms.query(query);
    }


    // you

    // term: you
    // sound: uuu
}

