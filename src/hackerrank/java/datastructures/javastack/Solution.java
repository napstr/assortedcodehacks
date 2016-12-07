package hackerrank.java.datastructures.javastack;

/**
 * Created by npstr on 06.12.2016
 */

import java.util.Scanner;
import java.util.Stack;

class Solution {

    public static void main(String[] argh) {
        Scanner sc = new Scanner(System.in);

        while (sc.hasNext()) {
            String input = sc.next();
            //Complete the code

            Stack<Integer> stack = new Stack<>();

            boolean result = true;
            charloop:
            for (char c : input.toCharArray()) {

                switch (c) {
                    case '(':
                        stack.push(0);
                        break;
                    case '[':
                        stack.push(1);
                        break;
                    case '{':
                        stack.push(2);
                        break;
                    case ')':
                        if (stack.size() < 1 || !stack.pop().equals(0)) {
                            result = false;
                            break charloop;
                        }
                        break;
                    case ']':
                        if (stack.size() < 1 || !stack.pop().equals(1)) {
                            result = false;
                            break charloop;
                        }
                        break;
                    case '}':
                        if (stack.size() < 1 || !stack.pop().equals(2)) {
                            result = false;
                            break charloop;
                        }
                        break;

                }
            }

            if (stack.size() > 0) result = false;
            System.out.println(result);

        }
    }
}
