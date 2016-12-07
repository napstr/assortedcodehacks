package thor;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int thorX = in.nextInt();
        int thorY = in.nextInt();
        Koords thor = new Koords(thorY, thorX);

        // game loop
        while (true) {
            int strikesLeft = in.nextInt(); // the remaining number of hammer strikes.
            int amountGiants = in.nextInt(); // the number of giants which are still present on the map.
            List<Koords> giants = new ArrayList<>();
            for (int i = 0; i < amountGiants; i++) {
                int x = in.nextInt();
                int y = in.nextInt();
                giants.add(new Koords(y, x));
            }

            System.err.println("Thor: " + thor);
            System.err.println("Giants: ");
            for (Koords k : giants) {
                System.err.println(k);
            }


            //strategy:
            //position thor in the center of all giants and let them come

            //whenever thor is in danger of getting hit by a giant (giants around his destination) he instead waits
            //if he is in danger by waiting (giants around his current location) he uses his strike

            Koords target = centerOf(giants);

            Data data = direction(thor, target);

            boolean stay = false;

            if (data.direction.equals("WAIT")) {
                stay = true;
            } else {
                if (inDanger(data.koords, giants)) {
                    stay = true;
                }
            }

            boolean strike = false;

            if (stay) {
                if (inDanger(thor, giants)) {
                    strike = true;
                }
            }

            String out;

            if (strike) {
                out = "STRIKE";
            } else if (stay) {
                out = "WAIT";
            } else {
                out = data.direction;
                thor = data.koords;
            }

            System.out.println(out); // The movement or action to be carried out: WAIT STRIKE N NE E SE S SW W or N
        }
    }

    private static boolean inDanger(Koords k, List<Koords> giants) {

        Koords tmp = k;

        if (giants.contains(tmp)) return true;

        //N
        tmp = new Koords(k.y - 1, k.x);
        if (giants.contains(tmp)) return true;

        //NE
        tmp = new Koords(k.y - 1, k.x + 1);
        if (giants.contains(tmp)) return true;

        //E
        tmp = new Koords(k.y, k.x + 1);
        if (giants.contains(tmp)) return true;

        //SE
        tmp = new Koords(k.y + 1, k.x + 1);
        if (giants.contains(tmp)) return true;

        //S
        tmp = new Koords(k.y + 1, k.x);
        if (giants.contains(tmp)) return true;

        //SW
        tmp = new Koords(k.y + 1, k.x - 1);
        if (giants.contains(tmp)) return true;

        //W
        tmp = new Koords(k.y, k.x - 1);
        if (giants.contains(tmp)) return true;

        //NW
        tmp = new Koords(k.y - 1, k.x - 1);
        if (giants.contains(tmp)) return true;


        return false;
    }

    private static Koords centerOf(List<Koords> koords) {
        double sumY = 0.0;
        double sumX = 0.0;

        for (Koords k: koords) {
            sumY += k.y;
            sumX += k.x;
        }

        return new Koords((int) Math.round(sumY / koords.size()), (int) Math.round(sumX / koords.size()));
    }

    private static Data direction(Koords current, Koords target) {

        String direction = "";
        Koords k = null;

        int vertical = target.y - current.y; //positive = move down = South
        int horizontal = target.x - current.x; //positive = move right = East




        if (horizontal < 0)

        { //move West
            if (vertical < 0) {//move North
                direction = "NW";
                k = new Koords(current.y - 1, current.x - 1);
            } else if (vertical == 0) {//stay vertically
                direction = "W";
                k = new Koords(current.y, current.x - 1);
            } else if (vertical > 0) {//move South
                direction = "SW";
                k = new Koords(current.y + 1, current.x - 1);
            }

        } else if (horizontal == 0)

        {//stay horizontally
            if (vertical < 0) {//move North
                direction = "N";
                k = new Koords(current.y - 1, current.x);
            } else if (vertical == 0) {//stay vertically
                //nothing
                direction = "WAIT";
                k = new Koords(current.y, current.x);
            } else if (vertical > 0) {//move South
                direction = "S";
                k = new Koords(current.y + 1, current.x);
            }

        } else if (horizontal > 0)

        {//move East
            if (vertical < 0) {//move North
                direction = "NE";
                k = new Koords(current.y - 1, current.x + 1);
            } else if (vertical == 0) {//stay vertically
                direction = "E";
                k = new Koords(current.y, current.x + 1);
            } else if (vertical > 0) {//move South
                direction = "SE";
                k = new Koords(current.y + 1, current.x + 1);
            }
        }

        return new Data(k, direction);
    }
}

class Data {
    Koords koords;
    String direction;

    public Data(Koords k, String d) {
        koords = k;
        direction = d;
    }
}

class Koords {
    int y;
    int x;
    public Koords(int y, int x) {
        this.y = y;
        this.x = x;
    }

    public Koords copy() {
        return new Koords(y, x);
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