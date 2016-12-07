package indiana;

import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int W = in.nextInt(); // number of columns.
        int H = in.nextInt(); // number of rows.
        in.nextLine();
        for (int i = 0; i < H; i++) {
            String LINE = in.nextLine(); // each line represents a line in the grid and contains W integers T. The absolute value of T specifies the type of the room. If T is negative, the room cannot be rotated.
        }
        int EX = in.nextInt(); // the coordinate along the X axis of the exit.
        in.nextLine();

        // game loop
        while (true) {
            int XI = in.nextInt();
            int YI = in.nextInt();
            String POSI = in.next();
            in.nextLine();
            int R = in.nextInt(); // the number of rocks currently in the grid.
            in.nextLine();
            for (int i = 0; i < R; i++) {
                int XR = in.nextInt();
                int YR = in.nextInt();
                String POSR = in.next();
                in.nextLine();
            }

            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");

            System.out.println("WAIT"); // One line containing on of three commands: 'X Y LEFT', 'X Y RIGHT' or 'WAIT'
        }
    }
}