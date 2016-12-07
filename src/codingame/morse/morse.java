package morse;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Solution {

    private static int possibilities = 0;
    private static char[][] words;

    public static void main(String args[]) throws FileNotFoundException {
        //Scanner in = new Scanner(System.in);
        Scanner in = new Scanner(new File("src\\codingame\\morse\\4.txt"));

        String mc = in.next();
        List<Character> morseCode = new ArrayList<>();
        for (char c : mc.toCharArray()) morseCode.add(c);
        int w = in.nextInt();
        words = new char[w][];
        for (int i = 0; i < w; i++) {
            String word = in.next();
            words[i] = (morsify(word));
        }

        goFullRecurse(morseCode);
        System.out.println(possibilities);
    }


    private static void goFullRecurse(List<Character> morseCode) {

        //stop condition
        if (morseCode.isEmpty()) {
            possibilities++;
            System.err.println(possibilities);
        }

        outerLoop:
        for (char[] word : words) {
            if (word.length > morseCode.size()) continue;
            int i = 0;
            for (char c : word) {
                if (c != morseCode.get(i)) {
                    continue outerLoop;
                }
                i++;
            }

            goFullRecurse(morseCode.subList(word.length, morseCode.size()));
        }
    }

    private static char[] morsify(String word) {
        String result = "";

        for (char c : word.toCharArray()) {
            switch (c) {
                case 'A':
                    result += ".-";
                    break;
                case 'B':
                    result += "-...";
                    break;
                case 'C':
                    result += "-.-.";
                    break;
                case 'D':
                    result += "-..";
                    break;
                case 'E':
                    result += ".";
                    break;
                case 'F':
                    result += "..-.";
                    break;
                case 'G':
                    result += "--.";
                    break;
                case 'H':
                    result += "....";
                    break;
                case 'I':
                    result += "..";
                    break;
                case 'J':
                    result += ".---";
                    break;
                case 'K':
                    result += "-.-";
                    break;
                case 'L':
                    result += ".-..";
                    break;
                case 'M':
                    result += "--";
                    break;
                case 'N':
                    result += "-.";
                    break;
                case 'O':
                    result += "---";
                    break;
                case 'P':
                    result += ".--.";
                    break;
                case 'Q':
                    result += "--.-";
                    break;
                case 'R':
                    result += ".-.";
                    break;
                case 'S':
                    result += "...";
                    break;
                case 'T':
                    result += "-";
                    break;
                case 'U':
                    result += "..-";
                    break;
                case 'V':
                    result += "...-";
                    break;
                case 'W':
                    result += ".--";
                    break;
                case 'X':
                    result += "-..-";
                    break;
                case 'Y':
                    result += "-.--";
                    break;
                case 'Z':
                    result += "--..";
                    break;
                default:
                    result += "";
                    System.err.println("Hit unknown character " + c + "  while morsifying. Not adding morse code.");
                    break;
            }
        }
        return result.toCharArray();
    }
}