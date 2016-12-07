package hackerrank.security.keyspaces;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Solution {

    public static void main(String[] args) {
        Scanner in;
        try {
            in = new Scanner(new File("src\\hackerrank\\security\\keyspaces\\test"));
        } catch (FileNotFoundException e) {
            in = new Scanner(System.in);
        }

        String s = in.nextLine();
        String result = "";

        for (int i = 0; i < s.length(); i++) {

            char c = s.charAt(i);
            int x = Integer.valueOf(c + "");

            x++;
            x %= 10;

            result += x;
        }

        System.out.println(result);
    }
}