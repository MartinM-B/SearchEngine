package sta.entity.interfaces;

import sta.entity.Term;

public interface StorageInterface {

    public void addTerm(String term, int fileId, String filePath, int position);
    public Term getTerm(String term);

}
