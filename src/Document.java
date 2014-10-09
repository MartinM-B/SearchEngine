import java.util.HashSet;
import java.util.Set;

public class Document {

    int id;
    Set<Integer> positions = new HashSet<>();


    //
    // Constructor
    public Document(int id) {
        this.id = id;
    }


    //
    // Additional Methods
    public void addPosition(int position) {
        positions.add(position);
    }


    //
    // Getter / Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Set<Integer> getPositions() {
        return positions;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Document document = (Document) o;

        if (id != document.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
