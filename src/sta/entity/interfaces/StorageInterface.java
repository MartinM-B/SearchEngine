package sta.entity.interfaces;

import sta.entity.Term;

<<<<<<< HEAD
import java.util.Set;
=======
import java.util.Collection;
>>>>>>> 32cd825c570b80d14ca95e10d90f99aafabae705

public interface StorageInterface {

    public void addTerm(String term, int fileId, String filePath, int position);
    public Term getTerm(String term);
<<<<<<< HEAD
    public Set<Integer> getAvailableDocuments();
=======
    public Collection<Term> query(String term);

>>>>>>> 32cd825c570b80d14ca95e10d90f99aafabae705

}
