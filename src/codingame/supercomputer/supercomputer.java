package supercomputer;

import java.util.*;

class Solution {

    static final List<Calc> allCalcs = new ArrayList<>();

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();

        for (int i = 0; i < n; i++) {
            allCalcs.add(new Calc(in.nextInt(), in.nextInt()));
        }
        Collections.sort(allCalcs);

        int count = 0;
        int progress = -1;
        for (Calc c : allCalcs) {
            if (c.end > progress && c.start > progress) {
                count++;
                progress = c.end;
            }
        }
        System.out.println(count);
    }
}

class Calc implements Comparable<Calc> {

    int start;
    int end;

    public Calc(int s, int d) {
        start = s;
        end = s + d - 1;
    }
    @Override
    public int compareTo(Calc o) {
        return end - o.end;
    }
}