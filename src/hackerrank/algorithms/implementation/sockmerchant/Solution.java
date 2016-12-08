package hackerrank.algorithms.implementation.sockmerchant;

/**
 * Created by npstr on 08.12.2016
 */

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Solution {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        Map<Integer, Integer> socks = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int sock = in.nextInt();
            socks.putIfAbsent(sock, 0);
            socks.put(sock, socks.get(sock) + 1);
        }
        int pairs = 0;
        for (int v : socks.values()) pairs += v/2;
        System.out.println(pairs);
    }
}
