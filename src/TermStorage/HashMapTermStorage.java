package TermStorage;

import foo.Term;

import java.util.HashMap;


public class HashMapTermStorage implements StorageInterface{

    HashMap<String, Term> terms = new HashMap<>();

    public HashMapTermStorage() {
    }

    @Override
    public void addTerm(String term, int fileId, String filePath, int position) {

    }
}
