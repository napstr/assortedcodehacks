package hackerrank.algorithms.implementation.minimaxsum;

/**
 * Created by npstr on 08.12.2016
 */

import java.util.Scanner;

public class Solution {

    private static long min = Long.MAX_VALUE;
    private static long max = Long.MIN_VALUE;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        long a = in.nextLong();
        long b = in.nextLong();
        long c = in.nextLong();
        long d = in.nextLong();
        long e = in.nextLong();

        long sum = a + b + c + d;
        minmax(sum);
        sum = a + b + c + e;
        minmax(sum);
        sum = a + b + d + e;
        minmax(sum);
        sum = a + c + d + e;
        minmax(sum);
        sum = b + c + d + e;
        minmax(sum);

        System.out.println(min + " " + max);
    }

    private static void minmax(long a) {
        if (a < min) min = a;
        if (a > max) max = a;
    }
}
