package sta.entity;

import sta.entity.interfaces.StorageInterface;
import sta.utils.BTree;

import java.util.HashSet;
import java.util.Set;


public class TermStorageBTree implements StorageInterface {

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
    public Set<Integer> getAvailableDocuments() {
        return availableDocuments;
    }
}
