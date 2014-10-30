package sta.entity.interfaces;

import sta.entity.Term;

import java.util.Collection;

public interface StorageInterface {

    public void addTerm(String term, int fileId, String filePath, int position);
    public Term getTerm(String term);
    public Collection<Term> query(String term);


}
