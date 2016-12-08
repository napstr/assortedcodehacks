package hackerrank.algorithms.implementation.repeatedstring;

/**
 * Created by npstr on 08.12.2016
 */

import java.util.Scanner;

public class Solution {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String s = in.next();
        long n = in.nextLong();

        int as = s.length() - s.replaceAll("a", "").length();
        int ssl = (int) (n % s.length());
        String ss = s.substring(0, ssl);
        int ssAs = ss.length() - ss.replaceAll("a", "").length();

        System.out.println(n / s.length() * as + ssAs);
    }
}
