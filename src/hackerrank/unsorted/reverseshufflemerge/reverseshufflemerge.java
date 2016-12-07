package hackerrank.unsorted.reverseshufflemerge;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

class Solution {

    public static void main(String[] args) {
        double time = System.currentTimeMillis();
        Scanner in;
        try {
            in = new Scanner(new File("src\\hackerrank\\unsorted\\reverseshufflemerge\\test"));
        } catch (FileNotFoundException e) {
            in = new Scanner(System.in);
        }
        String s = in.next();

        s = new StringBuilder(s).reverse().toString();

        Map<Character, Integer> m = new HashMap<>();

        for (char c : s.toCharArray()) {
            if (!m.containsKey(c))
                m.put(c, 0);
            m.put(c, m.get(c) + 1);
        }

        Map<Character, Integer> m2 = new HashMap<>();
        for (char c : m.keySet()) {
            m.put(c, m.get(c) / 2);
            m2.put(c, m.get(c));
        }

        String result = "";
        List<CharInt> chars = new ArrayList<>();

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            m.put(c, m.get(c) - 1);

            if (m2.get(c) > 0)

                chars.add(new CharInt(c, i));

            if (m.get(c) < 0) {

                boolean done = false;
                while (!done) {
                    //find smallest
                    CharInt d = chars.get(0);

                    for (CharInt ci : chars) {
                        if ((ci.c < d.c) || (ci.c == d.c && ci.i < d.i))
                            d = ci;
                    }

                    if (m2.get(d.c) > 0) {
                        m2.put(d.c, m2.get(d.c) - 1);
                        m.put(d.c, m.get(d.c) + 1);

                        result += d.c;
                        if (d.c == c)
                            done = true;

                        int cs = chars.size();
                        for (int j = 0; j < cs; j++) {
                            CharInt ci = chars.remove(0);
                            if (ci.i > d.i) {
                                chars.add(ci);
                            }
                        }
                    } else {
                        chars.remove(d);
                    }

                }
            }
        }
        System.err.println("time: " + (System.currentTimeMillis() - time));
        System.out.println(result);
    }
}

class CharInt {
    char c;
    int i;

    public CharInt(char c, int i) {
        this.c = c;
        this.i = i;
    }

    @Override
    public String toString() {
        return "(" + c + "," + i + ")";
    }
}