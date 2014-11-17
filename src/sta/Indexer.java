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
                            String term = processTerm(label.current());
                            position++;

                            if (term.length() > 0) {
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

            //terms.getTheString();

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

        if (this.terms.getTerm(processTerm(termName1)) == null || this.terms.getTerm(processTerm(termName2)) == null) {
            return new HashSet<Integer>();
        }

        Set<Integer> positions = this.terms.getTerm(processTerm(termName1)).getDocumentIds();
        Set<Integer> positions1 = this.terms.getTerm(processTerm(termName2)).getDocumentIds();

        positions.retainAll(positions1);
        return positions;
    }

    public Set<Integer> merge(Set positions, String termName2) {

        if (this.terms.getTerm(processTerm(termName2)) == null || positions.isEmpty()) {
            return new HashSet<Integer>();
        }

        Set<Integer> positions2 = this.terms.getTerm(processTerm(termName2)).getDocumentIds();

        positions.retainAll(positions2);
        return positions;
    }

    public Set<Integer> merge(String[] termNames) {
        if (termNames.length <= 0)
            return null;

        Term t = terms.getTerm(processTerm(termNames[0]));

        if (t == null)
            return null;

        Set<Integer> positions = t.getDocumentIds();

        if (positions == null)
            return null;

        for (String term : termNames) {
            positions = this.merge(positions, term);
            //positions.retainAll(this.terms.getTerm(processTerm(term)).getDocumentIds());
        }
        return positions;
    }

    public Set or(String termName1, String termName2) {
        Term term = this.terms.getTerm(processTerm(termName1));
        Term term2 = this.terms.getTerm(processTerm(termName2));

        if (term == null && term2 == null) {
            return new HashSet<Integer>();
        }

        if (term == null) {
            return term2.getDocumentIds();
        }

        if (term2 == null) {
            return term.getDocumentIds();
        }
        Set<Integer> positions = term.getDocumentIds();
        Set<Integer> positions1 = term2.getDocumentIds();

        positions.addAll(positions1);
        return positions;
    }

    public Set<Integer> or(Set<Integer> positions, String termName2) {
        Term term = this.terms.getTerm(processTerm(termName2));

        if (term == null) {
            return positions;
        }

        Set<Integer> positions2 = term.getDocumentIds();
        positions.addAll(positions2);
        return positions;
    }

    public Set<Integer> or(String[] termNames) {
        Term t = this.terms.getTerm(processTerm(termNames[0]));

        Set<Integer> positions;

        if(t == null) {
            positions = new HashSet<>();
        } else {
            positions = t.getDocumentIds();
        }

        for (String term : termNames) {
            positions = or(positions, term);
        }
        return positions;
    }

    public Set not(String termName1) {
        Term term1 = this.terms.getTerm(processTerm(termName1));
        if (term1 == null) {
            return this.terms.getAvailableDocuments();
        }
        Set<Integer> positions = term1.getDocumentIds();
        Set<Integer> positions2 = this.terms.getAvailableDocuments();

        positions2.removeAll(positions);
        return positions2;
    }

    private String processTerm(String term) {
        term = removeSiblings(term);
        term = stemming(term);
        return term;
    }

    public Collection<Term> searchWithWildcard(String query) {
        return terms.query(query);
    }

    /**
     *
     * @param type 0: getDictionaryString
     *             1: createDictionaryBlockedString
     *             2: createFrontcodingString
     * @return
     */
    public String createDictionaryString(int type) {

        switch (type){
            case 0:
                return this.terms.getDictionaryString();
            case 1:
                return this.terms.createDictionaryBlockedString();
            case 2:
                return this.terms.createFrontcodingString();
        }
        return null;
    }

    public String getTermsString(){
        return this.terms.toString();
    }
}

