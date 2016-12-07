package voxcodei;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

class Player {

    private static int width;
    private static int height;

    private static int round = 0;

    private static final boolean DEBUG = false;

    public static void main(String args[]) throws FileNotFoundException {

        Scanner in;
        if (DEBUG) in = new Scanner(new File("R:\\Dropbox\\Programmieren\\einsnull\\src\\2.txt"));
        else in = new Scanner(System.in);

        width = in.nextInt(); // width of the firewall grid
        height = in.nextInt(); // height of the firewall grid

        Square[][] map;

        map = new Square[height][];

        for (int y = 0; y < height; y++) {
            String mapRow = in.next(); // one line of the firewall grid
            map[y] = new Square[width];

            for (int x = 0; x < width; x++) {
                int i;
                switch (mapRow.charAt(x)) {
                    case '.': //empty
                        i = 0;
                        break;
                    case '@': //node
                        i = 1;
                        break;
                    case '#': //unbreakable node
                        i = 2;
                        break;
                    default:
                        i = -1;
                        System.err.println("Jet fuel can't melt steel beams: " + mapRow.charAt(x));
                }

                map[y][x] = new Square(i, new Koords(y, x), -1);
            }
        }


        int rounds = in.nextInt(); // number of rounds left before the end of the game
        int bombs = in.nextInt(); // number of bombs left

        if (DEBUG) {
            for (Square[] asd : map) {
                String out = "";
                for (Square sq : asd) {
                    out += sq.what;
                }
                System.err.println(out);
            }
            System.err.println("Rounds: " + rounds + ", Bombs: " + bombs);
        }


        RecursiveResult rr = calculateEverythingRecursive(rounds, bombs, map);

        if (DEBUG) {
            System.err.println(rr.success);
            if (rr.success) {
                for (Target t : rr.targets) {
                    System.err.println(t.koords);
                }
            }
        }

        // game loop
        //rr.targets contains the targets that need to get blown up in a reverse order
        int index = rr.targets.size();
        while (true) {
            index--;
            round++;
            String out;

            //while there are targets
            if (index >= 0) {
                Target t = rr.targets.get(index);
                //invalid coordinates are used to indicate that the best option at this point is to wait
                if (t.koords.x == -1) out = "WAIT";
                else {
                    //this little if-clause lets us see into the future
                    if (map[t.koords.y][t.koords.x].detonatesInRound >= round) {
                        out = "WAIT";
                        index++;
                    }
                    //finally, if we made it here we can detonate the bomb
                    else {
                        out = t.koords.x + " " + t.koords.y;
                        detonateBomb(map, t.koords);
                    }
                }
            }
            //no targets left
            else out = "WAIT";

            System.out.println(out);

            rounds = in.nextInt();
            bombs = in.nextInt();
        }
    }

    private static RecursiveResult calculateEverythingRecursive(int roundsLeft, int bombsLeft, Square[][] currentMap) {

        bombsLeft--;
        roundsLeft--;

        List<Target> targets = getTargets(currentMap);

        int index = 0;
        while (index < targets.size()) {

            //create a copy of the current map
            Square[][] copyOfCurrentMap = copyMap(currentMap);

            //explode a bomb on it
            detonateBomb(copyOfCurrentMap, targets.get(index).koords);


            //stop conditions reached?
            // map cleared? -> return true
            // or
            // no bombs (or rounds) left? -> try the next index

            if (isMapClear(copyOfCurrentMap)) {
                List<Target> result = new ArrayList<>();
                result.add(targets.get(index));
                return (new RecursiveResult(true, result));
            } else if (bombsLeft == 0 || roundsLeft == 0) {
                index++;
                continue;
            }

            //start the next call
            RecursiveResult rr = calculateEverythingRecursive(roundsLeft, bombsLeft, copyOfCurrentMap);
            if (rr.success) {
                rr.targets.add(targets.get(index));
                return rr;
            }

            //code came here? pretty unlikely, but lets just increase the index by one
            index++;
        }

        //if this is reached the call could not find a valid solution, there wont be a valid solution
        return new RecursiveResult(false, null);
    }

    private static boolean isMapClear(Square[][] map) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (map[y][x].what == 1) return false;
            }
        }
        return true;
    }

    private static Square[][] copyMap(Square[][] map) {
        Square[][] result = new Square[height][];

        for (int y = 0; y < height; y++) {
            result[y] = new Square[width];
            for (int x = 0; x < width; x++){

                result[y][x] = map[y][x].copy();
            }
        }
        return result;
    }


    private static List<Target> getTargets(Square[][] currentMap) {

        List<Target> result = new ArrayList<>();
        //this represents the "WAIT" option
        //doesn't have any effect in the current state, but may be needed in later version of the puzzle
        //result.add(new Target(new Koords(-1, -1), Integer.MAX_VALUE));

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Koords k = new Koords(y, x);
                int destruction = destructionCaused(currentMap, k, false);
                result.add(new Target(k, destruction));
            }
        }

        Collections.sort(result);

        //if we don't cut the results to only the relevant one we get a stackoverflow exception in our recursive code
        //in this case: relevant = the five best places to detonate a bomb

        List<Target> targets = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            targets.add(result.get(i));
        }
        result = targets;

        return result;
    }


    private static void detonateBomb(Square[][] map, Koords k) {
        destructionCaused(map, k, true);
    }


    private static int destructionCaused(Square[][] map, Koords k, boolean detonateForReal) {
        if (k.y == -1) {
            return Integer.MAX_VALUE;
        }

        //can place a bomb only on a free tile
        if (map[k.y][k.x].what != 0) return 0;

        int result = 0;

        //look 3 up
        Koords k1 = new Koords(k.y - 1, k.x);
        Koords k2 = new Koords(k.y - 2, k.x);
        Koords k3 = new Koords(k.y - 3, k.x);
        result += howManyNodesExplode(map, k1, k2, k3, detonateForReal);

        //look 3 right
        k1 = new Koords(k.y, k.x + 1);
        k2 = new Koords(k.y, k.x + 2);
        k3 = new Koords(k.y, k.x + 3);
        result += howManyNodesExplode(map, k1, k2, k3, detonateForReal);

        //look 3 down
        k1 = new Koords(k.y + 1, k.x);
        k2 = new Koords(k.y + 2, k.x);
        k3 = new Koords(k.y + 3, k.x);
        result += howManyNodesExplode(map, k1, k2, k3, detonateForReal);

        //look 3 left
        k1 = new Koords(k.y, k.x - 1);
        k2 = new Koords(k.y, k.x - 2);
        k3 = new Koords(k.y, k.x - 3);
        result += howManyNodesExplode(map, k1, k2, k3, detonateForReal);

        return result;
    }

    private static int howManyNodesExplode(Square[][] map, Koords k1, Koords k2, Koords k3, boolean explode) {
        int result = 0;

        if (validKoords(k1)) {
            boolean blocked = false;
            if (map[k1.y][k1.x].what == 1) {
                result++;
                if (explode) {
                    map[k1.y][k1.x].what = 0;
                    map[k1.y][k1.x].detonatesInRound = round + 2;
                }
            }
            if (map[k1.y][k1.x].what == 2) blocked = true;

            if (validKoords(k2) && !blocked) {
                if (map[k2.y][k2.x].what == 1) {
                    result++;
                    if (explode) {
                        map[k2.y][k2.x].what = 0;
                        map[k2.y][k2.x].detonatesInRound = round + 2;
                    }
                }
                if (map[k2.y][k2.x].what == 2) blocked = true;

                if (validKoords(k3) && !blocked) {
                    if (map[k3.y][k3.x].what == 1) {
                        result++;
                        if (explode) {
                            map[k3.y][k3.x].what = 0;
                            map[k3.y][k3.x].detonatesInRound = round + 2;
                        }
                    }
                }
            }
        }
        return result;
    }

    private static boolean validKoords(Koords k) {
        if (k.y < 0 || k.y >= height || k.x < 0 || k.x >= width) return false;
        return true;
    }

}

class RecursiveResult {
    boolean success;
    List<Target> targets;

    public RecursiveResult(boolean s, List<Target> t) {
        success = s;
        targets = t;
    }
}

class Square {
    int what; //0 free, 1 node, 2 blocked
    Koords k;
    int detonatesInRound = -1;

    public Square(int what, Koords k, int detonatesInRound) {
        this.what = what;
        this.k = k;
        this.detonatesInRound = detonatesInRound;
    }

    public Square copy() {
        return new Square(what, k.copy(), detonatesInRound);
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

class Target implements Comparable<Target> {
    Koords koords;
    int destruction;

    public Target(Koords k, int d) {
        koords = k;
        destruction = d;
    }

    @Override
    public int compareTo(Target o) {
        return o.destruction - destruction;
    }
}