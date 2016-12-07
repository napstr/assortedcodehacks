package hackerrank.mathematics.findpoint;

import java.util.Scanner;

public class Solution {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int t = in.nextInt();
        for (int i = 0; i < t; i++) {
            int pX = in.nextInt();
            int pY = in.nextInt();
            int qX = in.nextInt();
            int qY = in.nextInt();

            int rX = qX + (qX - pX);
            int rY = qY + (qY - pY);

            String result = rX + " " + rY;
            System.out.println(result);
        }
    }
}
