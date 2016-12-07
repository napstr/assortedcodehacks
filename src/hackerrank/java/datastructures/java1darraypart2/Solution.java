package hackerrank.java.datastructures.java1darraypart2;
/**
 * Created by npstr on 06.12.2016
 */


import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Solution {

    private static Set<Integer> beenHere;

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        int t = in.nextInt();
        for (int i = 0; i < t; i++) {
            int n = in.nextInt();
            int m = in.nextInt();
            beenHere = new HashSet<>();

            int[] arr = new int[n];
            for (int a = 0; a < n; a++) {
                arr[a] = in.nextInt();
            }

            String out = "NO";
            if (recursive(arr, 0, m)) out = "YES";
            System.out.println(out);

        }
    }

    private static boolean recursive(int[] arr, int position, int m) {

        //have we been here?
        if (beenHere.contains(position)) return false;
        beenHere.add(position);

        if (position < 0) return false; //wrong direction lol

        if (position >= arr.length) return true; //yay we did it

        if (arr[position] == 1) return false; //stepped on a 1


        //take the three paths
        if (recursive(arr, position + 1, m)) return true;
        if (recursive(arr, position - 1, m)) return true;
        if (recursive(arr, position + m, m)) return true;

        //none of the paths reached the end
        return false;
    }
}