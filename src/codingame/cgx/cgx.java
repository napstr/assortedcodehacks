package cgx;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class Solution {
    static String result;
    static int deep;

    public static void main(String args[]) throws FileNotFoundException {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        in.nextLine();

        String cleanInformation = "";
        boolean inString = false;

        //read all the information while removing \n, \r, \t and spaces. unless inside a 'string'
        for (int i = 0; i < n; i++) {
            String cGXLine = in.nextLine();

            for (char c : cGXLine.toCharArray()) {

                if (inString || (c != '\n' && c != '\r' && c != ' ' && c != '\t')) {
                    cleanInformation += c;
                }
                if (c == '\'') inString = !inString;
            }
        }


        result = "";
        deep = 0;
        inString = false;

        //this indicates that there already has been a newline which happens after (, ) and ;
        //so we can detect when these character are chained and prevent the code from creating an entire empty line between them
        //(which the code does by poor design)
        boolean lastCharWasParenthesis = true;

        for (char c : cleanInformation.toCharArray()) {

            if (inString) {
                //if were inside a string we don't ask any questions. at all.
                result += c;
            } else {

                //block starts
                if (c == '(') {
                    if (!lastCharWasParenthesis) {
                        out();
                    }

                    result += c;
                    deep++;
                    out();
                    lastCharWasParenthesis = true;
                }

                //block ends
                else if (c == ')') {
                    deep--;
                    if (!lastCharWasParenthesis) {
                        out();
                    } else {
                        result = result.substring(0, result.length() - 4);
                    }

                    result += c;
                    out();
                    lastCharWasParenthesis = true;
                }

                //line ends
                else if (c == ';') {

                    if (result.endsWith(" ")) {
                        int end = result.lastIndexOf("\n");
                        result = result.substring(0, end);
                    }

                    result += c;
                    out();
                    lastCharWasParenthesis = true;
                }

                else {
                    result += c;
                    lastCharWasParenthesis = false;
                }
            }

            //keep track of being inside a string
            if (c == '\'') inString = !inString;
        }



        System.out.println(result);
    }

    private static void out() {
        result += '\n';
        result += addSpaces();
    }

    private static String addSpaces() {
        String result = "";
        for (int i = 0; i < deep; i++) {
            result += "    ";
        }
        return  result;
    }
}