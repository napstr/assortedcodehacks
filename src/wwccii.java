import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * Created by npstr on 03.05.2016.
 */
public class wwccii {


    private static String[] names = {"npstr",
            "aedan777",
            "JermanTK",
            "Spockyt",
            "Capt. Kiwi",
            "Ithvan",
            "Tornadoli",
            "Daffius",
            "Ironhide G1",
            "Jacksonian Missionary",
            "Cliges",
            "Dr.Livingstone",
            "Gen. Skobelev",
            "Yakman",
            "Kaisersohaib",
            "Yahmik",
            "Comm Cody",
            "Lemeard",
            "Capibara",
            "Chieron",
            "madchemist",
            "Rovsea",
            "Audren",
            "al-Aziz",
            "Gorganslayer",
            "Wagonlitz",
            "deathbywombat",
            "AVN"};


    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {

        int a = 0;
        int b = 1;
        int c = 2;
        int d = 3;
        int e = 4;


        PrintWriter writer = new PrintWriter("output.txt", "UTF-8");

        for (a = 0; a < names.length - 4; a++) {
            for (b = a + 1; b < names.length - 3; b++) {
                for (c = b + 1; c < names.length - 2; c++) {
                    for (d = c + 1; d < names.length - 1; d++) {
                        for (e = d + 1; e < names.length; e++) {

                            writer.println(names[a] + ", " + names[b] + ", " + names[c] + ", " + names[d] + " & " + names[e]);

                            writer.println(names[a] + ", " + names[b] + ", " + names[c] + " & " + names[d]);
                            writer.println(names[a] + ", " + names[b] + ", " + names[c] + " & " + names[e]);
                            writer.println(names[a] + ", " + names[b] + ", " + names[d] + " & " + names[e]);
                            writer.println(names[a] + ", " + names[c] + ", " + names[d] + " & " + names[e]);
                            writer.println(names[b] + ", " + names[c] + ", " + names[d] + " & " + names[e]);

                            writer.println(names[a] + ", " + names[b] + " & " + names[c]);
                            writer.println(names[a] + ", " + names[b] + " & " + names[d]);
                            writer.println(names[a] + ", " + names[b] + " & " + names[e]);
                            writer.println(names[a] + ", " + names[c] + " & " + names[d]);
                            writer.println(names[a] + ", " + names[c] + " & " + names[e]);
                            writer.println(names[a] + ", " + names[d] + " & " + names[e]);
                            writer.println(names[b] + ", " + names[c] + " & " + names[d]);
                            writer.println(names[b] + ", " + names[c] + " & " + names[e]);
                            writer.println(names[b] + ", " + names[d] + " & " + names[e]);
                            writer.println(names[c] + ", " + names[d] + " & " + names[e]);


                        }
                    }
                }
            }
        }

        writer.close();
    }
}
