package hackerrank.java.introduction.javaloopsI;

import java.util.Scanner;

public class Solution {

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        Scanner sc = new Scanner(System.in);

        int a = sc.nextInt();

        for (int i = 1; i < 11; i++) {
            System.out.println(String.format("%1$s x %2$s = %3$s", a, i, a * i));
        }
    }
}