package hackerrank.security.cryptoanalysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Solution {

    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    private static final char[] ABC = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    public static void main(String[] args) {

        long time = System.currentTimeMillis();

        Scanner in;
        try {
            in = new Scanner(new File("src\\hackerrank\\security\\cryptoanalysis\\test"));
        } catch (FileNotFoundException e) {
            in = new Scanner(System.in);
        }

        Scanner dictScan = null;
        try {
            dictScan = new Scanner(new File("src\\hackerrank\\security\\cryptoanalysis\\dictionary.lst"));
        } catch (FileNotFoundException e) {
            try {
                dictScan = new Scanner(new File("dictionary.lst"));
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        }

        //read the dictionary
        List<String> dict = new ArrayList<>();
        while (dictScan.hasNextLine()) {
            dict.add(dictScan.nextLine().toLowerCase());
        }

        List<String> words = new ArrayList<>();
        while (in.hasNext()) {
            words.add(in.next());
        }

        Map<String, List<String>> theRiddle = new HashMap<>();

        for (String s : words) {
            theRiddle.put(s, new ArrayList<>());
            for (String d : dict) {
                if (haveSamePattern(s, d)) {
                    theRiddle.get(s).add(d);
                }
            }
        }

        char[] key = new char[26];
        for (int i = 0; i < 26; i++) key[i] = ' ';
        boolean makingProgress = true;

        while (!keyIsComplete(key) && makingProgress) {
            makingProgress = false;

            List<String> toRemove = new ArrayList<>();

            //look for words that only have one fitting pattern and add their info to the key
            for (String s : theRiddle.keySet()) {
                if (theRiddle.get(s).size() == 1) {
                    String found = theRiddle.get(s).get(0);

                    for (int i = 0; i < s.length(); i++) {
                        char cF = found.charAt(i);
                        char cS = s.charAt(i);

                        int index = ALPHABET.indexOf(cF + "");

                        key[index] = cS;
                    }
                    toRemove.add(s);
                }
            }
            for (String s : toRemove)
                theRiddle.remove(s);

            //clean the left patterns with the new information of the key
            for (String s : theRiddle.keySet()) {
                List<String> founds = theRiddle.get(s);

                List<String> toRemoveF = new ArrayList<>();

                for (String f : founds) {
                    for (int i = 0; i < s.length(); i++) {

                        char cF = f.charAt(i);
                        char cS = s.charAt(i);

                        char k = key[ALPHABET.indexOf(cF + "")];
                        if (k != cS && k != ' ') {
                            //not fitting anymore
                            toRemoveF.add(f);
                        }

                    }
                }

                for (String f : toRemoveF) {
                    founds.remove(f);
                    makingProgress = true;
                }


            }
//            System.out.println(key);
        }

//        for (String s : theRiddle.keySet()) {
//            System.out.println("str:" + s);
//            for (String f : theRiddle.get(s)) {
//                System.out.println(f);
//            }
//        }

        String result = "";
        for (String s : words) {
            result += " " + decrypt(s, key);
        }
        result = result.substring(1);

//        System.out.println((System.currentTimeMillis() - time) + "ms");

        System.out.println(result);
    }

    private static boolean keyIsComplete(char[] key) {
        for (char c : key) {
            if (c == ' ') return false;
        }

        return true;
    }

    private static String encrypt(String input, char[] key) {

        String result = "";
        String keyStr = new String(key);

        for (char c : input.toCharArray()) {
            result += "" + keyStr.charAt(ALPHABET.indexOf("" + c));
        }

        return result;
    }

    private static String decrypt(String cipher, char[] key) {
        String result = "";
        String keyStr = new String(key);

        for (char c : cipher.toCharArray()) {
            result += "" + ALPHABET.charAt(keyStr.indexOf("" + c));
        }

        return result;

    }

    private static boolean haveSamePattern(String a, String b) {

        return pattern(a).equals(pattern(b));

//        if (a.length() != b.length())
//            return false;
//
//
//        while (a.length() > 0) {
//
//            char aC = a.charAt(0);
//            a = a.substring(1);
//
//            char bC = b.charAt(0);
//            b = b.substring(1);
//
//
//            while (a.indexOf("" + aC) > -1) {
//
//                int index = a.indexOf("" + aC);
//
//                if (b.charAt(index) == bC) {
//                    a = a.substring(0, index) + a.substring(index + 1);
//                    b = b.substring(0, index) + b.substring(index + 1);
//                } else {
//                    return false;
//                }
//            }
//        }
//        return true;
    }

    private static String pattern(String s) {

        char[] result = new char[s.length()];
        char[] input = s.toCharArray();

        int index = 0;
        for (int i = 0; i < input.length; i++) {
            char c = input[i];
            if (s.indexOf("" + c) < i) {
                result[i] = result[s.indexOf("" + c)];
            } else {
                result[i] = ABC[index];
                index++;
            }
        }
        return new String(result);
    }
}