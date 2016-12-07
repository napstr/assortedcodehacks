package hackerrank.java.strings.javastringsintroduction;

import java.util.Scanner;

public class Solution {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String a = sc.next();
        String b = sc.next();
        System.out.println(a.length() + b.length());

        if (a.compareTo(b) > 0) System.out.println("Yes");
        else System.out.println("No");

        String result = "";
        result += a.substring(0, 1).toUpperCase();
        result += a.substring(1);
        result += " ";
        result += b.substring(0, 1).toUpperCase();
        result += b.substring(1);
        System.out.println(result);
    }
}
