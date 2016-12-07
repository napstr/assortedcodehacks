package hackerrank.java.datastructures.javaarraylist;

/**
 * Created by npstr on 06.12.2016
 */

import java.util.ArrayList;
import java.util.Scanner;

public class Solution {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int a = in.nextInt();
        in.nextLine();
        ArrayList<Integer>[] arrs = new ArrayList[a];

        for (int i = 0; i < a; i++) {
            arrs[i] = new ArrayList<>();
            String line = in.nextLine();
            String[] parsed = line.split(" ");

            for (String s : parsed) {
                int n = Integer.valueOf(s);
                arrs[i].add(n);
            }
        }

        int b = in.nextInt();

        for (int i = 0; i < b; i++) {
            int x = in.nextInt() - 1;
            int y = in.nextInt();

            System.err.println(String.format("x: %1$s, y: %2$s", x, y));

            String out = "ERROR!";
            if (x < arrs.length && y < arrs[x].size()) {
                out = arrs[x].get(y) + "";
            }
            System.out.println(out);
        }
    }
}