package hackerrank.mathematics.possiblepath;

import java.util.Scanner;

public class Solution {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int t = in.nextInt();
        for (int i = 0; i < t; i++) {

            int a = in.nextInt();
            int b = in.nextInt();
            int x = in.nextInt();
            int y = in.nextInt();

            if ((x % a != 0) || (y % b != 0)) {
                System.out.println("NO");
            } else {
                System.out.println("YES");
            }
        }
    }
}