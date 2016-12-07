package hackerrank.java.datastructures.javasubarray;


/**
 * Created by npstr on 06.12.2016
 */

import java.util.Scanner;

public class Solution {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int t = in.nextInt();
        int[] arr = new int[t];
        for (int i = 0; i < t; i++) {
            arr[i] = in.nextInt();
        }

        int result = 0;
        for (int x = 0; x < t; x++) {
            for (int y = x; y < t; y++) {

                int sum = 0;
                for (int a = x; a <= y; a++) {
                    sum += arr[a];
                }
                if (sum < 0) result++;
            }
        }
        System.out.println(result);
    }
}