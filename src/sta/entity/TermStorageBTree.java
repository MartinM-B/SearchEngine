package sta.entity;

import sta.entity.interfaces.StorageInterface;
import sta.utils.BTree;

import java.util.*;


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

    public String getDictionaryString(){
        String s = "";
        for(Object t : this.terms.toStringKey() ){
            Term temp = (Term) t;
            temp.setPosition(s.length());
            s += temp.getName();
        }
        return s;
    }

    public String createDictionaryBlockedString() {
        String s = "";
        for(Object t : this.terms.toStringKey() ){
            Term temp = (Term) t;
            temp.setPosition(s.length());
            s += temp.getName().length();
            s += temp.getName();
        }
        return s;
    }

    private static final int frontCodingCuttingLength = 4;

    public String createFrontcodingString() {
        String s = "";
        Term before = null;

        HashMap<String, List> blub = new HashMap<>();

        for(Object t : this.terms.toStringKey() ){
            Term temp = (Term) t;
            temp.setPosition(s.length());

            //---

            if(compareStringNames(temp, before, frontCodingCuttingLength) >= frontCodingCuttingLength){
                String key = temp.getName().substring(0, frontCodingCuttingLength);
                if(blub.containsKey(key)){
                    blub.get(key).add(temp.getName());
                }else{
                    List tempList = new ArrayList<String>();
                    tempList.add(temp.getName());
                    blub.put(key, tempList);
                }
            }else{
                blub.put(temp.getName(), null);
            }
//            s += temp.getName().length();
//            s += temp.getName();

            //---
            before = (Term) t;
        }

        for (Map.Entry<String, List> entry : blub.entrySet()) {

            if(entry.getValue() == null){
                s += entry.getKey().length();
                s += entry.getKey();
            }else{
                s += this.frontCodeString(entry.getValue());
            }

        }


        return s;
    }

    private String frontCodeString(List<String> terms){
        String s = (frontCodingCuttingLength+1) + terms.get(0).substring(0, frontCodingCuttingLength) + "*";

        int counter = 0;
        for(String termToCut : terms){
            String temp = termToCut.substring(frontCodingCuttingLength);
            s += temp + temp.length();
            if(counter < terms.size()){
                s += "◊";
            }
            counter++;
        }
        return s;
    }

    private int compareStringNames(Term one, Term two, int position) {

        if(one == null || two == null){
            return 0;
        }

        if(one.getName().length() < position || two.getName().length() < position){
            return position-1;
        }

        String s1 = one.getName().substring(0, position);
        String s2 = two.getName().substring(0, position);

        if(s1.equals(s2)) {
           return compareStringNames(one, two, position+1);
        }else{
            return position;
        }
    }

}

