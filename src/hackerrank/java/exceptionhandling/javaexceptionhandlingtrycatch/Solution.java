package hackerrank.java.exceptionhandling.javaexceptionhandlingtrycatch;

/**
 * Created by npstr on 07.12.2016
 */

import java.util.InputMismatchException;
import java.util.Scanner;

public class Solution {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        try {
            int x = in.nextInt();
            int y = in.nextInt();

            System.out.println(x / y);
        } catch (InputMismatchException e) {
            System.out.println("java.util.InputMismatchException");
        } catch (ArithmeticException e) {
            System.out.println(e.toString());
        }
    }
}