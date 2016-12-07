package hackerrank.algorithms.warmup.circulararrayrotation;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by npstr on 06.12.2016
 */
public class Solution {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt(); //array length
        int k = in.nextInt(); //rotations
        k = k % n;
        int q = in.nextInt(); //queries
        ArrayList<Integer> a = new ArrayList();
        for (int a_i = 0; a_i < n; a_i++) {
            a.add(a_i, in.nextInt());
        }
        for (int rot = 0; rot < k; rot++) {
            a.add(0, a.remove(n - 1));
        }

        for (int a0 = 0; a0 < q; a0++) {
            int m = in.nextInt();
            System.out.println(a.get(m));
        }
    }
}
