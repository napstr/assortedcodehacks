package hackerrank.unsorted.savehumanity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Solution {

    public static void main(String[] args) {
        Scanner in;
        try {
            in = new Scanner(new File("src\\hackerrank\\unsorted\\savehumanity\\t1"));
        } catch (FileNotFoundException e) {
            System.err.println("File not found, using System.in");
            in = new Scanner(System.in);
        }
        int t = in.nextInt();
        double time;
        for (int i = 0; i < t; i++) {
            time = System.currentTimeMillis();
            String p = in.next();
            String v = in.next();

            List<Comparing> list = new ArrayList<>();
            List<Integer> result = new ArrayList<>();

            int n = 0;
            for (char c : p.toCharArray()) {

                list.add(new Comparing(v, n));

                int j = list.size();
                for (int k = 0; k < j; k++) {
                    Comparing comp = list.remove(0);

                    char cc = comp.v.remove(0);

                    if (cc == c) {

                    } else if (!comp.mistake) {
                        comp.mistake = true;
                    } else {
                        continue; //no recall
                    }

                    if (comp.v.size() == 0)
                        result.add(comp.start);
                    else
                        list.add(comp);

                }
                n++;
            }

            String out = "";
            if (result.size() == 0) out = "No Match!";
            else {
                for (Integer o : result) out += " " + o;
                out = out.substring(1);
            }
            System.out.println(out);
            System.err.println("Time: " + (System.currentTimeMillis() - time));
        }
    }
}

class Comparing {
    boolean mistake = false;
    List<Character> v = new ArrayList<>();
    int start;

    public Comparing(String virus, int s) {
        for (char c : virus.toCharArray()) v.add(c);
        start = s;
    }
}