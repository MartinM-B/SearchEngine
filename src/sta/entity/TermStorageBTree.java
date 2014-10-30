package sta.entity;

import sta.entity.interfaces.StorageInterface;
import sta.utils.BTree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class TermStorageBTree implements StorageInterface {
    public static final String WILDCARD = "$";


    BTree<String, Term> terms = new BTree<>();

    @Override
    public void addTerm(String term, String soundex, int fileId, String filePath, int position) {
        Term t = terms.get(term);

        if (t == null)
            t = new Term(term, soundex);

        t.addPosition(fileId, filePath, position);
        terms.put(term, t);
    }

    @Override
    public Term getTerm(String term) {
        return terms.get(term);
    }

    @Override
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
    }
}
