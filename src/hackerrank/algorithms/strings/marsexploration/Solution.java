package hackerrank.algorithms.strings.marsexploration;

/**
 * Created by npstr on 08.12.2016
 */

import java.util.Scanner;

public class Solution {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String s = in.next();

        int count = 0;
        for (int i = 0; i < s.length() / 3; i++) {
            if (s.charAt(i * 3) != 'S') count++;
            if (s.charAt(i * 3 + 1) != 'O') count++;
            if (s.charAt(i * 3 + 2) != 'S') count++;
        }
        System.out.println(count);
    }
}
