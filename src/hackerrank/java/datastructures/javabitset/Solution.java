package hackerrank.java.datastructures.javabitset;

/**
 * Created by npstr on 07.12.2016
 */

import java.util.BitSet;
import java.util.Scanner;

public class Solution {

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int m = in.nextInt();

        BitSet b1 = new BitSet();
        BitSet b2 = new BitSet();
        for (int i = 0; i < n; i++) {
            b1.set(i, false);
            b2.set(i, false);
        }

        for (int i = 0; i < m; i++) {
            String instruction = in.next();
            int x = in.nextInt();
            int y = in.nextInt();

            if ("AND".equals(instruction)) {
                if (x == 1) b1.and(b2);
                else b2.and(b1);
            } else if ("OR".equals(instruction)) {
                if (x == 1) b1.or(b2);
                else b2.or(b1);
            } else if ("XOR".equals(instruction)) {
                if (x == 1) b1.xor(b2);
                else b2.xor(b1);
            } else if ("FLIP".equals(instruction)) {
                if (x == 1) b1.flip(y);
                else b2.flip(y);
            } else if ("SET".equals(instruction)) {
                if (x == 1) b1.set(y);
                else b2.set(y);
            }

            System.out.println(b1.cardinality() + " " + b2.cardinality());

        }
    }
}
