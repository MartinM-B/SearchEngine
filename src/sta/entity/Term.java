package sta.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Term implements Comparable<Term> {

    String name;
    int position;
    String soundex;
    int frequency;
    List<Document> documents = new ArrayList<>();

    //
    // Constructor
    public Term(String name, String soundex) {

        this.name = name;
        this.soundex = soundex;
        this.position = 0;
    }

    //
    // Additional Methods
    public void addPosition(int id, String filename, int position) {
        Document document = null;
        try {
            for (Document d : documents) {
                if (d.getId() == id) {
                    document = d;
                }
            }
        } catch (Exception ignored) {
        }

        if (document == null) {
            document = new Document(id, filename);
            documents.add(document);
        }
        document.addPosition(position);
        frequency++;
    }


    //
    // Getter / Setter
    public Set<Integer> getDocumentIds() {

        Set<Integer> ids = new HashSet<>();
        for (Document d : documents) {
            ids.add(d.getId());
        }

        return ids;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSoundex() {
        return soundex;
    }

    public void setSoundex(String soundex) {
        this.soundex = soundex;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public List<Document> getDocuments() {
        return documents;
    }


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Term term = (Term) o;

        if (name.equals(term.name)) return true;
        if (soundex.equals(term.soundex)) return true;

        return false;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public int compareTo(Term o) {
        if (this == o) return 0;

        if (name.compareTo(o.getName()) == -1 ) return -1;
        if (soundex.compareTo(o.getSoundex()) == -1) return -1;

        return 1;
    }

    @Override
    public String toString() {
        return "Term {" +
                "name='" + name + '\'' +
                "soundex='" + soundex+ '\'' +
                '}';
    }
}
