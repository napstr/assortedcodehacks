package hackerrank.security.permutations;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Solution {

    public static void main(String[] args) {
        Scanner in;
        try {
            in = new Scanner(new File("src\\hackerrank\\security\\permutations\\test"));
        } catch (FileNotFoundException e) {
            in = new Scanner(System.in);
        }

        int t = in.nextInt();
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 1; i <= t; i++) {
            int x = in.nextInt();
            map.put(i, x);
        }


        for (int i = 1; i <= t; i++) {
            System.out.println(map.get(map.get(i)));
        }
    }
}