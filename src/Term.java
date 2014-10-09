import java.util.ArrayList;
import java.util.List;

public class Term {

    String name;
    int frequency;
    List<Document> documents = new ArrayList<>();


    //
    // Constructor
    public Term(String name) {
        this.name = name;
    }

    //
    // Additional Methods
    public void addPosition(int id, int position) {
        Document document = null;
        try {
            document = documents.get(id);
        } catch (Exception e) {
        }

        if (document == null) {
            document = new Document(id);
            documents.add(document);
        }
        document.addPosition(position);
        frequency++;
    }


    //
    // Getter / Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Term term = (Term) o;

        if (name != null ? !name.equals(term.name) : term.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
