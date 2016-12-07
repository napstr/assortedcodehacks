package surface;

import java.io.FileNotFoundException;
import java.util.*;

class Solution {

    static boolean[][] map;
    static int width;
    static int height;

    public static void main(String args[]) throws FileNotFoundException {
        Scanner in = new Scanner(System.in);
        width = in.nextInt();
        height = in.nextInt();

        map = new boolean[height][width];
        for (int y = 0; y < height; y++) {
            String row = in.next();
            for (int x = 0; x < row.length(); x++) {
                char c = row.charAt(x);
                if (c == '#') {
                    map[y][x] = false;
                } else {
                    map[y][x] = true;
                }
            }
        }

        int amountTests = in.nextInt();
        List<Koords> tests = new ArrayList<>();
        for (int i = 0; i < amountTests; i++) {
            int x = in.nextInt();
            int y = in.nextInt();
            tests.add(new Koords(y, x));
        }

        for (Koords testKoords : tests) {
            System.out.println(lakeSize(testKoords));
        }
    }

    //a flood fill using a stacked approach because the recursive one results in a stack overflow (how ironic)
    private static int lakeSize(Koords koords) {
        int result = 0;

        //reset processed
        boolean[][] processed = new boolean[height][width];

        LinkedList<Koords> queue = new LinkedList<>();
        queue.add(koords);

        while (!queue.isEmpty()) {
            Koords current = queue.pollFirst();

            if (processed[current.y][current.x]) {
                continue;
            }

            processed[current.y][current.x] = true;

            if (map[current.y][current.x]) {
                result++;

                if (current.y - 1 >= 0) {
                    queue.addLast(new Koords(current.y - 1, current.x));
                }

                if (current.y + 1 < height) {
                    queue.addLast(new Koords(current.y + 1, current.x));
                }

                if (current.x - 1 >= 0) {
                    queue.addLast(new Koords(current.y, current.x - 1));
                }

                if (current.x + 1 < width) {
                    queue.addLast(new Koords(current.y, current.x + 1));
                }
            }
        }
        return result;
    }
}

class Koords {
    int y;
    int x;
    public Koords(int y, int x) {
        this.y = y;
        this.x = x;
    }
    
    @Override
    public boolean equals(Object o) {
        Koords k = (Koords) o;
        if (this.y == k.y && this.x == k.x) return true;
        return false;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + y;
        result = prime * result + x;
        return result;
    }
    
    @Override
    public String toString() {
        return "(" + y + "|" + x + ")";
    }
}