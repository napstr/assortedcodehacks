package rollercoaster;

import java.util.*;

class Solution {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int totalSeats = in.nextInt();
        int ridesPerDay = in.nextInt();
        int amountOfGroups = in.nextInt();
        int[] queue = new int[amountOfGroups];

        for (int i = 0; i < amountOfGroups; i++) {
            queue[i] = in.nextInt();
        }

        long money = 0;
        int queueIndex = 0;
        Map<Integer, Data> memo = new HashMap<>();

        for (int r = 0; r < ridesPerDay; r++) {

            int freeSeats = totalSeats;
            int groupMoney = 0;

            //memoized?
            if (memo.get(queueIndex) != null) {
                Data d = memo.get(queueIndex);
                groupMoney = d.money;
                queueIndex = d.next;
            }
            else {
                int groupStartIndex = queueIndex;
                int groupCounter = 0;

                boolean done = false;
                while (!done) {
                    int nextGroup = queue[queueIndex];
                    groupCounter++;

                    //ride is full when the group doesn't fit or all groups from the queue are aboard
                    if (nextGroup <= freeSeats && groupCounter <= amountOfGroups) {
                        groupMoney += nextGroup;
                        freeSeats -= nextGroup;
                        queueIndex++;
                        if (queueIndex == amountOfGroups) queueIndex = 0;
                    } else {
                        done = true;
                        memo.put(groupStartIndex, new Data(groupMoney, queueIndex));
                    }
                }
            }

            money += groupMoney;
        }
        System.out.println(money);
    }
}

class Data {

    int money;
    int next;

    public Data(int m, int n) {
        money = m;
        next = n;
    }

}