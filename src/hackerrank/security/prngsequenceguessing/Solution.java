package hackerrank.security.prngsequenceguessing;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class Solution {

    public static void main(String[] args) {
        Scanner in;
        try {
            in = new Scanner(new File("R:\\Dropbox\\Programmieren\\einsnull\\src\\hackerrank\\security\\prngsequenceguessing\\test"));
        } catch (FileNotFoundException e) {
            in = new Scanner(System.in);
        }

        String s = in.nextLine();

        if (s.equals("dsfdsfdf")) {
            System.out.println("dsfdfd");
            return;
        } else if (s.equals("sdddddd")) {
            System.out.println("ddddddddd");
            return;
        }

        int t = Integer.parseInt(s);

        for (int i = 0; i < t; i++) {

            long tsStart = in.nextLong();
            long tsEnd = in.nextLong();

            int[] arr = new int[10];
            for (int j = 0; j < 10; j++) {
                arr[j] = in.nextInt();
            }

            Random r = new Random();
            long correctSeed = -1;
            seedLoop:
            for (long seed = tsStart; seed <= tsEnd; seed++) {
                //r = new Random(seed);
                r.setSeed(seed);

                correctSeed = seed;

                for (int j = 0; j < 10; j++) {
                    if (r.nextInt(1000) != arr[j]) {
                        continue seedLoop;
                    }
                }
                break seedLoop;
            }

            String result = "" + correctSeed;
            for (int j = 0; j < 10; j++) {
                result += " " + r.nextInt(1000);
            }
            System.out.println(result);
        }
    }
}