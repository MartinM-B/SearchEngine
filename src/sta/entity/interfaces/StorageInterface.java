package sta.entity.interfaces;

import sta.entity.Term;

import java.util.Set;
import java.util.Collection;

public interface StorageInterface {

    public void addTerm(String term, String soundex, int fileId, String filePath, int position);
    public Term getTerm(String term);
    public Set<Integer> getAvailableDocuments();
    public Collection<Term> query(String term);
    public void getTheString();
}
