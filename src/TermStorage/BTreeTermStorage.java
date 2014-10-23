package TermStorage;

import data_structures.BTree;
import foo.Term;


public class BTreeTermStorage implements StorageInterface {

    BTree<String, Term> terms = new BTree<String, Term>();

    public BTreeTermStorage() {
    }

    public void addTerm(String term, int fileId, String filePath, int position) {
        Term t = terms.get(term);

        if (t == null)
            t = new Term(term);

        t.addPosition(fileId, filePath, position);
        terms.put(term, t);
    }


}
