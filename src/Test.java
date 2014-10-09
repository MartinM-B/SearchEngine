import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class Test {


    public static void main(String[] args) {
        Set<Integer> s1 = new HashSet<>();
        Set<Integer> s2 = new HashSet<>();

        Random random = new Random();

        s1.addAll(random.ints(20, 0, 30).distinct().boxed().collect(Collectors.toSet()));
        s2.addAll(random.ints(20, 0, 30).distinct().boxed().collect(Collectors.toSet()));
        System.out.println(s1);
        System.out.println(s2);
        //System.out.println(Indexer.intersect(s1, s2));
    }
}
