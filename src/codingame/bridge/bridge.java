package bridge;

import java.util.Scanner;

class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int m = in.nextInt(); // the amount of motorbikes to control
        int v = in.nextInt(); // the minimum amount of motorbikes that must survive
        String l0 = in.next(); // L0 to L3 are lanes of the road. A dot character . represents a safe space, a zero 0 represents a hole in the road.
        String l1 = in.next();
        String l2 = in.next();
        String l3 = in.next();

        // game loop
        while (true) {
            int s = in.nextInt(); // the motorbikes' speed
            for (int i = 0; i < m; i++) {
                int x = in.nextInt(); // x coordinate of the motorbike
                int y = in.nextInt(); // y coordinate of the motorbike
                int a = in.nextInt(); // indicates whether the motorbike is activated "1" or detroyed "0"
            }

            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");

            System.out.println("SPEED"); // A single line containing one of 6 keywords: SPEED, SLOW, JUMP, WAIT, UP, DOWN.
        }
    }
}