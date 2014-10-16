import org.tartarus.snowball.SnowballStemmer;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Scanner;
import java.util.Set;

public class Indexer {

    public static final String LIBRARY = "org.tartarus.snowball.ext.germanStemmer";
    TermStorage terms = new TermStorage();
    int fileId = 1;
    SnowballStemmer stemmer;


    public Indexer() {
        try {
            Class stemClass = Class.forName(LIBRARY);
            stemmer = (SnowballStemmer) stemClass.newInstance();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public void parseFolder(String path) {
        try {
            Files.walk(Paths.get(path)).forEach(filePath -> {
                if (Files.isRegularFile(filePath)) {
                    File file = new File(filePath.toString());
                    Scanner input = null;

                    try {
                        input = new Scanner(file);
                        int position = 0;


                        while (input.hasNext()) {
                            ++position;
                            String term = input.next();


                            term = tokenizer(term);
                            term = stemming(term);

                            if (!term.equals("")) {
                                if (!terms.contains(term)) {
                                    Term t = new Term(term);
                                    terms.addTerm(t);
                                }

                                terms.getTerm(term).addPosition(fileId, position);
                            }
                        }

                        input.close();
                        fileId++;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String tokenizer(String nextToken) {
        return nextToken.replaceAll("[^a-zA-ZüöäÖÄÜß]", "").toLowerCase();
    }

    private String stemming(String token) {
        stemmer.setCurrent(token);
        stemmer.stem();
        return stemmer.getCurrent();
    }

    public Collection<Integer> intersect(Set<Integer> s1, Set<Integer> s2) {
        s1.retainAll(s2);
        return s1;
    }
}
