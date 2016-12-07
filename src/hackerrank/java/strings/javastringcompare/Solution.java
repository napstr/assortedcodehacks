package hackerrank.java.strings.javastringcompare;

        import java.util.Scanner;

public class Solution {

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        Scanner in = new Scanner(System.in);
        String s = in.next();
        int k = in.nextInt();
        String smallest = s.substring(0, k);
        String largest = s.substring(0, k);
        for (int i = 1; i <= s.length() - k; i++) {
            String sub = s.substring(i, i + k);
            if (sub.compareTo(smallest) < 0) smallest = sub;
            if (sub.compareTo(largest) > 0) largest = sub;
        }
        System.out.println(smallest);
        System.out.println(largest);
    }
}
