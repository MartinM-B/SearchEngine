package sta.entity;

import sta.entity.interfaces.StorageInterface;
import sta.utils.BTree;

import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class TermStorageBTree implements StorageInterface {
    public static final String WILDCARD = "$";
    public static String DictionaryString ="";


    BTree<String, Term> terms = new BTree<>();
    Set<Integer> availableDocuments = new HashSet<Integer>();

    @Override
    public void addTerm(String term, String soundex, int fileId, String filePath, int position) {
        Term t = terms.get(term);

        if (t == null) {
            t = new Term(term, soundex);
            terms.put(term, t);
        }

        t.addPosition(fileId, filePath, position);
        this.availableDocuments.add(fileId);



    }

    @Override
    public Term getTerm(String term) {
        try{
            return terms.get(term);
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public Set<Integer> getAvailableDocuments() {
        return availableDocuments;
    }

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

    public void getTheString(){

        System.out.println(this.terms.toStringKey());

    }
    public void createDictionaryString() {
        String temp = "";
        int count = 0;



    }
}

