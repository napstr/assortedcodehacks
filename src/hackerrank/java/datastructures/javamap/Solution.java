package hackerrank.java.datastructures.javamap;

/**
 * Created by npstr on 06.12.2016
 */

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class Solution {
    public static void main(String[] argh) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        in.nextLine();
        Map<String, Integer> book = new HashMap<>();
        for (int i = 0; i < n; i++) {
            String name = in.nextLine();
            int phone = in.nextInt();
            in.nextLine();
            book.put(name, phone);
        }

        while (in.hasNext()) {
            String s = in.nextLine();
            String out = "Not found";
            if (book.containsKey(s)) out = String.format("%1$s=%2$s", s, book.get(s));
            System.out.println(out);
        }
    }
}