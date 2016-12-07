package hackerrank.security.bijective;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Solution {

    public static void main(String[] args) {

        Scanner in;
        try {
            in = new Scanner(new File("R:\\Dropbox\\Programmieren\\einsnull\\src\\hackerrank\\security\\bijective\\test"));
        } catch (FileNotFoundException e) {
            in = new Scanner(System.in);
        }

        int t = in.nextInt();
        List<Integer> arr = new ArrayList<>();
        for (int i = 0; i < t; i++) {
            int x = in.nextInt();
            if (arr.contains(x)) {
                System.out.println("NO");
                return;
            } else {
                arr.add(x);
            }
        }
        System.out.println("YES");
    }
}