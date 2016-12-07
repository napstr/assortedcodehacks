package hackerrank.java.datastructures.javadequeue;


/**
 * Created by npstr on 07.12.2016
 */

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;


public class Solution {
    public static void main(String[] args) throws Exception {
        Scanner in;
        try {
            in = new Scanner(new FileReader("R:\\Dropbox\\Programmieren\\einsnull\\input.txt"));
        } catch (FileNotFoundException e) {
            in = new Scanner(System.in);
        }
        int n = in.nextInt();
        int m = in.nextInt();
        int mc = m;

        List<Integer> list = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            list.add(in.nextInt());
        }

        int max = Integer.MIN_VALUE;
        Map<Integer, Integer> map = new HashMap<>(m, 1f);
        for (int j = 0; j < list.size(); j++) {
            int num = list.get(j);
            if (!map.containsKey(num)) map.put(num, 0);
            map.put(num, map.get(num) + 1);

            mc--;
            if (mc <= 0) {
                if (mc < 0) {
                    int i = list.get(j - m);
                    map.put(i, map.get(i) - 1);
                    if (map.get(i) <= 0) map.remove(i);
                }
                if (map.size() > max) max = map.size();
            }
        }
        System.out.println(max);
    }
}
