package hackerrank.java.datastructures.javadequeue;

/**
 * Created by npstr on 07.12.2016
 * <p>
 * this one was not efficient enough, last few test timed out
 * decided to make an implementation with a HashMap instead of the Deque to keep a continuous count if the different numbers
 */

import java.util.*;

public class Solution2 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Deque<Integer> deque = new ArrayDeque<>();
        int n = in.nextInt();
        int m = in.nextInt();

        int max = Integer.MIN_VALUE;
        for (int i = 0; i < n; i++) {
            int num = in.nextInt();
            deque.addLast(num);
            m--;
            if (m < 0) {
                deque.removeFirst();
                int u = uniques(deque);
                if (u > max) max = u;
            }
        }
        System.out.println(max);
    }

    private static int uniques(Deque d) {
        Set set = new HashSet();
        set.addAll(d);
        return set.size();
    }
}
