package hackerrank.java.bignumber.javabigdecimal;

/**
 * Created by npstr on 06.12.2016
 */

import java.math.BigDecimal;
import java.util.Scanner;

class Solution {
    public static void main(String[] args) {
        //Input
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        String[] s = new String[n + 2];
        for (int i = 0; i < n; i++) {
            s[i] = sc.next();
        }
        sc.close();


        //Write your code here

        for (int a = 0; a < n - 1; a++) {
            for (int b = 0; b < n - 1; b++) {

                BigDecimal x = new BigDecimal(s[b]);
                BigDecimal y = new BigDecimal(s[b + 1]);

                if (x.compareTo(y) < 0) {
                    String tmp = s[b + 1];
                    s[b + 1] = s[b];
                    s[b] = tmp;
                }

            }
        }


        //Output
        for (int i = 0; i < n; i++) {
            System.out.println(s[i]);
        }
    }
}