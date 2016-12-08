package hackerrank.algorithms.strings.camelcase;

/**
 * Created by npstr on 08.12.2016
 */

import java.util.Scanner;

public class Solution {

    private static final String ABC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String s = in.next();
        int count = 1;
        for (char c : s.toCharArray())
            if (ABC.contains(c + "")) count++;
        System.out.println(count);
    }
}
