package hackerrank.java.strings.javaanagrams;

import java.util.Scanner;

public class Solution {

    static boolean isAnagram(String a, String b) {
        a = a.toLowerCase();
        b = b.toLowerCase();
        if (a.length() != b.length()) return false;

        for (char c : a.toCharArray()) {
            int i = b.indexOf(c);
            if (i < 0) return false;
            else {
                b = b.substring(0, i) + b.substring(i + 1, b.length());
            }
        }
        return true;
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        String A = sc.next();
        String B = sc.next();
        boolean ret = isAnagram(A, B);
        if (ret) System.out.println("Anagrams");
        else System.out.println("Not Anagrams");

    }
}
