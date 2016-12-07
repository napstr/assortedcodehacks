package hackerrank.java.datastructures.javalist;

/**
 * Created by npstr on 06.12.2016
 */

import java.util.ArrayList;
import java.util.Scanner;

public class Solution {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            list.add(in.nextInt());
        }

        int q = in.nextInt();

        for (int i = 0; i < q; i++) {
            String action = in.next();
            int index = in.nextInt();
            if ("Insert".equals(action)) {
                int value = in.nextInt();
                list.add(index, value);
            } else {
                list.remove(index);
            }
        }

        String out = "";
        for (Integer i : list) {
            out += " " + i;
        }
        out = out.substring(1);
        System.out.println(out);
    }
}