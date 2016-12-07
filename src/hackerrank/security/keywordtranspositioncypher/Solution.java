package hackerrank.security.keywordtranspositioncypher;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Solution {

    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static void main(String[] args) {
        Scanner in;
        try {
            in = new Scanner(new File("src\\hackerrank\\security\\keywordtranspositioncypher\\test"));
        } catch (FileNotFoundException e) {
            in = new Scanner(System.in);
        }


        int t = Integer.parseInt(in.nextLine());

        for (int i = 0; i < t; i++) {
            String secret = in.nextLine().toUpperCase();
            String cipher = in.nextLine().toUpperCase();

            String table = createTable(secret);

            String result = "";
            for (char c : cipher.toCharArray()) {
                if (c == ' ') result += " ";
                else {
                    result += "" + ALPHABET.charAt(table.indexOf("" + c));
                }
            }

            System.out.println(result);
        }
    }

    private static String createTable(String secret) {

        String abc = ALPHABET;

        String t = "";
        for (char c : secret.toCharArray()) {
            int i = abc.indexOf(c + "");
            if (i >= 0) {
                t += c;
                abc = abc.substring(0, i) + abc.substring(i + 1);
            }
        }

        int len = t.length();
        String[] table = new String[len];


        for (int i = 0; i < len; i++) {
            table[i] = "" + t.charAt(i);
        }

        for (int i = 0; i < abc.length(); i++) {
            table[i % len] += "" + abc.charAt(i);
        }

        Arrays.sort(table);

        String result = "";
        for (String s : table)
            result += s;

        return result;
    }
}
