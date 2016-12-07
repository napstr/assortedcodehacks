package hackerrank.java.datastructures.java2darray;

/**
 * Created by npstr on 06.12.2016
 */

import java.util.Scanner;

public class Solution {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int data[][] = new int[6][6];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                data[i][j] = in.nextInt();
            }
        }

        int max = Integer.MIN_VALUE;
        for (int a = 0; a < 4; a++) {
            for (int b = 0; b < 4; b++) {
                int sum = 0;
                sum += data[a][b] + data[a][b + 1] + data[a][b + 2];
                sum += data[a + 1][b + 1];
                sum += data[a + 2][b] + data[a + 2][b + 1] + data[a + 2][b + 2];

                if (sum > max) max = sum;
            }
        }
        System.out.println(max);
    }
}
