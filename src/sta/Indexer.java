package sta;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import libs.snowball.SnowballStemmer;
import sta.entity.TermStorageBTree;
import sta.entity.interfaces.StorageInterface;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
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
                            String term = processTerm(label.current());
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


    /**
     *
     * @param termName1
     * @param termName2
     * @return
     */
    public Set merge(String termName1, String termName2) {
        Set<Integer> positions = this.terms.getTerm(processTerm(termName1)).getDocumentIds();
        Set<Integer> positions1 = this.terms.getTerm(processTerm(termName2)).getDocumentIds();

        positions.retainAll(positions1);
        return positions;
    }


    public Set merge (Set positions, String termName2){
        Set<Integer> positions2 = this.terms.getTerm(processTerm(termName2)).getDocumentIds();

        positions.retainAll(positions2);
        return positions;
    }

    public Set merge (String [] termNames){

        Set<Integer> positions =  this.terms.getTerm(processTerm(termNames[0])).getDocumentIds();
        for(String term : termNames){
            //positions = this.merge(positions, term);
            positions.retainAll(this.terms.getTerm(processTerm(term)).getDocumentIds());
        }
        return positions;
    }

    public Set or(String termName1, String termName2) {
        Set<Integer> positions = this.terms.getTerm(processTerm(termName1)).getDocumentIds();
        Set<Integer> positions1 = this.terms.getTerm(processTerm(termName2)).getDocumentIds();

        positions.addAll(positions1);
        return positions;
    }

    public Set or (Set positions, String termName2){
        Set<Integer> positions2 = this.terms.getTerm(processTerm(termName2)).getDocumentIds();

        positions.addAll(positions2);
        return positions;
    }

    public Set or (String [] termNames){

        Set<Integer> positions =  this.terms.getTerm(processTerm(termNames[0])).getDocumentIds();
        for(String term : termNames){
            positions = this.or(positions, term);
            //positions.addAll(this.terms.getTerm(processTerm(term)).getDocumentIds());
        }
        return positions;
    }

    public Set not(String termName1) {
        Set<Integer> positions = this.terms.getTerm(processTerm(termName1)).getDocumentIds();
        Set<Integer> positions2 = this.terms.getAvailableDocuments();

        positions2.removeAll(positions);
        return positions2;
    }

    private String processTerm(String term){
        term = removeSiblings(term);
        term = stemming(term);
        return term;
    }
}



