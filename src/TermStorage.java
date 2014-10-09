import java.util.HashMap;

/**
 * Created by tobiascurth on 09/10/14.
 */
public class TermStorage {

    HashMap<String, Term> terms = new HashMap<String, Term>();

    public TermStorage() {
    }

    public Term getTerm(String term) {
        return terms.get(term);
    }

    public void addTerm(Term term) {
        terms.put(term.getName(), term);
    }

    public boolean contains(String term) {
        return terms.containsKey(term);
    }

}
