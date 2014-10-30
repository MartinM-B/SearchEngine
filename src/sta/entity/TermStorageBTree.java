package sta.entity;

import sta.entity.interfaces.StorageInterface;
import sta.utils.BTree;


public class TermStorageBTree implements StorageInterface {

    BTree<String, Term> terms = new BTree<>();

    public void addTerm(String term, int fileId, String filePath, int position) {
        Term t = terms.get(term);

        if (t == null)
            t = new Term(term);

        t.addPosition(fileId, filePath, position);
        terms.put(term, t);
    }

    @Override
    public Term getTerm(String term) {
        return terms.get(term);
    }
}
