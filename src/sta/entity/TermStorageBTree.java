package sta.entity;

import sta.entity.interfaces.StorageInterface;
import sta.utils.BTree;

<<<<<<< HEAD
import java.util.HashSet;
import java.util.Set;
=======
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
>>>>>>> 32cd825c570b80d14ca95e10d90f99aafabae705


public class TermStorageBTree implements StorageInterface {
    public static final String WILDCARD = "$";


    BTree<String, Term> terms = new BTree<>();
    Set<Integer> availableDocuments = new HashSet<Integer>();

    public void addTerm(String term, int fileId, String filePath, int position) {
        Term t = terms.get(term);

        if (t == null)
            t = new Term(term);

        t.addPosition(fileId, filePath, position);
        this.availableDocuments.add(fileId);
        terms.put(term, t);
    }

    @Override
    public Term getTerm(String term) {
        return terms.get(term);
    }

    @Override
<<<<<<< HEAD
    public Set<Integer> getAvailableDocuments() {
        return availableDocuments;
=======
    public Collection<Term> query(String query) {
        List<Term> c = new ArrayList<>();
        boolean hasWildcard = false;

        if (query.contains(WILDCARD)) {
            query = query.replace(WILDCARD, "");
            hasWildcard = true;
        }

        if(!hasWildcard) {
            c.add(terms.get(query));
        } else {
            c.addAll(terms.getWithChilds(query));
        }

        return c;
>>>>>>> 32cd825c570b80d14ca95e10d90f99aafabae705
    }
}
