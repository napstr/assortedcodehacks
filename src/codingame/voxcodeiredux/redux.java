package voxcodeiredux;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

class Player {

    static int width;
    static int height;

    private static int currentRound = 0;

    private static int rounds;
    private static int roundsLeft;
    private static int bombs;
    private static int bombsLeft;

    private static final boolean DEBUG = false;

    private static Square[][] rootMap;

    public static void main(String args[]) throws FileNotFoundException {

        Scanner in;
        if (DEBUG)
            in = new Scanner(new File("src\\codingame\\redux\\2.txt"));
        else
            in = new Scanner(System.in);

        width = in.nextInt(); // width of the firewall grid
        height = in.nextInt(); // height of the firewall grid

        //round 0
        int currentRound = 0;
        rounds = in.nextInt(); // number of rounds left before the end of the game
        bombs = in.nextInt(); // number of bombs left
        rootMap = readMap(in);
        Map map = new Map(rootMap, rounds);
        System.out.println("WAIT");

        //we need at least 2 maps to calculate the moving of the nodes
        //round 1
        currentRound++;
        roundsLeft = in.nextInt();
        bombsLeft = in.nextInt();
        map.allMaps[currentRound] = readMap(in);

        //TODO: prediction can be done in less turns with a different (and maybe even faster) algorithm that compares the maps.
        //TODO: if we will run out of turns this is the place to fix it
        //TODO: Test 7 (and maybe 8) fails probably because of the current solution
        int predictionDone = 0;
        while (predictionDone < 1) {

            //create prediction based on the read data
            map.predict(currentRound);

            //read the next map and check if prediction is correct
            System.out.println("WAIT");
            currentRound++;
            roundsLeft = in.nextInt();
            bombsLeft = in.nextInt();
            Square[][] nextMap = readMap(in);

            if (map.compareMap(currentRound, nextMap))
                predictionDone++;
            else
                predictionDone = 0;
        }

        System.err.println("Prediction done in round " + currentRound);

        List<List<Detonation>> allPossibleDetonations = calculatePossibleDetonations(currentRound, map);

        //		System.err.println(allPossibleDetonations.size() + " classes of detonations found");
        //		for (List<Detonation> detonations : allPossibleDetonations)
        //		{
        //			System.err.println(detonations.get(0).toString());
        //		}

        List<List<Detonation>> boiledDown = boilDownToRelevantDetonations(allPossibleDetonations);
        System.err.println("Boiled down:");
        System.err.println(boiledDown.size() + " classes of detonations found");
        for (List<Detonation> detonations : boiledDown) {
            System.err.println(detonations.get(0).toString());
        }

        System.err.println(bombs + " out of " + boiledDown.size());

        if (boiledDown.size() > bombs) {
            boiledDown = boilDownFurther(boiledDown, bombs);
        }

        //

        System.err.println("More boiling:");
        System.err.println(boiledDown.size() + " classes of detonations found");
        for (List<Detonation> detonations : boiledDown) {
            System.err.println(detonations.get(0).toString());
        }

        //game loop
        while (true) {
            String out = "";

            if (boiledDown.size() > 0) {
                boolean found = false;
                int index = 0;
                while (!found && index < boiledDown.size()) {
                    List<Detonation> ds = boiledDown.get(index);
                    for (Detonation d : ds) {
                        if (d.round - 3 == currentRound) {
                            out += d.koords.x + " " + d.koords.y;
                            boiledDown.remove(index);
                            found = true;
                            break;
                        }
                    }
                    index++;
                }
                if (!found)
                    out = "WAIT";
            } else
                out = "WAIT";

            System.out.println(out);
            currentRound++;
            roundsLeft = in.nextInt();
            bombsLeft = in.nextInt();
            Square[][] nextMap = readMap(in);

            if (!map.compareMap(currentRound, nextMap)) {
                System.err.println("Prediction failed");
            }

            map.printMap(currentRound);
        }
    }

    private static List<List<Detonation>> boilDownFurther(List<List<Detonation>> allDetonations, int max) {

        List<List<Detonation>> result = new ArrayList<>();

        boolean validResult = false;

        while (!validResult) {

            result = new ArrayList<>(allDetonations);

            //sort by amount of nodes destroyed
            //			Collections.sort(result, (o1, o2) -> o2.get(0).nodes.size() - o1.get(0).nodes.size());

            //everyday im shuffling
            Collections.sort(result, (o1, o2) -> (int) (Math.round(100 * Math.random()) - 50));

            validResult = true;

            while (result.size() > max) {

                boolean found = false;
                List<Detonation> redundantClass = null;
                for (List<Detonation> detonations : result) {
                    //if all nodes of this class of detonations are covered by the other detonations we don't really  need this class of detonations

                    //nodes to check
                    List<Node> nodes = new ArrayList<>(detonations.get(0).nodes);

                    for (List<Detonation> otherDetonations : result) {

                        //don't compare to itself
                        if (detonations.equals(otherDetonations))
                            continue;

                        nodes.removeAll(otherDetonations.get(0).nodes);

                    }

                    //found redundant class of detonations
                    if (nodes.size() == 0) {
                        found = true;
                        redundantClass = detonations;
                        break;
                    }

                }

                if (found) {
                    result.remove(redundantClass);
                } else {
                    System.err.println("reshuffling");
                    validResult = false;
                    break;
                }

            }
        }
        return result;
    }

    //removing subsets may be problematic in the following scenario that in makes detonating a target impossible because the subset contained
    //the only valid detonations for a specific node, the superset detonations cant be placed (only one bomb per round)
    private static List<List<Detonation>> boilDownToRelevantDetonations(List<List<Detonation>> allPossibleDetonations) {

        List<List<Detonation>> boiledDown = new ArrayList<>();

        //remove empty detonations and detonations that are a subset of other detonations
        //in other words: keep only 'unique' detonations

        for (List<Detonation> detonations : allPossibleDetonations) {

            Detonation d = detonations.get(0);

            boolean isSubset = false;

            for (List<Detonation> ds : allPossibleDetonations) {
                if (d.isSubsetOf(ds.get(0)))
                    isSubset = true;
            }
            if (!isSubset)
                boiledDown.add(detonations);

        }

        return boiledDown;
    }

    private static List<List<Detonation>> calculatePossibleDetonations(int currentRound, Map map) {

        List<List<Detonation>> result = new ArrayList<>();

        //currentRound + 3 = round we can start looking for detonations

        for (int r = currentRound + 3; r < map.allMaps.length; r++) {

            Square[][] mapAtRoundR = map.allMaps[r];

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    Koords k = new Koords(y, x);
                    Detonation d = new Detonation(r, k, destructionCaused(map, r, k));

                    //check if there already is a list for this kind of detonation
                    boolean needAnotherList = true;
                    for (List<Detonation> detonations : result) {
                        if (detonations.get(0).detonatesSameNodes(d)) {
                            detonations.add(d);
                            needAnotherList = false;
                            break;
                        }
                    }
                    if (needAnotherList) {
                        List<Detonation> detonations = new ArrayList<>();
                        detonations.add(d);
                        result.add(detonations);
                    }
                }
            }
        }

        return result;
    }

    private static Square[][] readMap(Scanner in) {

        Square[][] map = new Square[height][];

        for (int y = 0; y < height; y++) {
            String mapRow = in.next(); // one line of the firewall grid
            map[y] = new Square[width];

            for (int x = 0; x < width; x++) {
                int i;
                switch (mapRow.charAt(x)) {
                    case '.': //empty
                        i = Square.EMPTY;
                        break;
                    case '@': //node
                        i = Square.NODE;
                        break;
                    case '#': //obstacle
                        i = Square.OBSTACLE;
                        break;
                    default:
                        i = -1;
                        System.err.println("Jet fuel can't melt steel beams: " + mapRow.charAt(x));
                }

                map[y][x] = new Square(i, new Koords(y, x), -1);
            }
        }

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

        return map;
    }

    public static Square[][] copyMapWithoutNodes(Square[][] map) {
        Square[][] result = new Square[height][];

        for (int y = 0; y < height; y++) {
            result[y] = new Square[width];
            for (int x = 0; x < width; x++) {
                result[y][x] = map[y][x].copy();
                if (result[y][x].what == Square.NODE) {
                    result[y][x].what = Square.EMPTY;
                }
            }
        }
        return result;
    }

    //returns a list of nodes that would be destroyed by an explosion of a bomb at location k in round r
    private static List<Node> destructionCaused(Map map, int r, Koords k) {
        List<Node> result = new ArrayList<>();
        Square[][] specificMap = map.allMaps[r];

        //can place a bomb only on a tile without an obstacle
        if (specificMap[k.y][k.x].what == Square.OBSTACLE)
            return result;

        //if 3 rounds ago there was a node here we can't place the bomb
        if (map.allMaps[r - 3][k.y][k.x].what == Square.NODE)
            return result;

        //if there is now a node on top of the bomb it will count
        if (specificMap[k.y][k.x].what == Square.NODE)
            result.add(specificMap[k.y][k.x].node);

        int index = 0;
        while (index < 4) {
            Koords k1 = null;
            Koords k2 = null;
            Koords k3 = null;

            switch (index) {
                case 0: //UP
                    k1 = new Koords(k.y - 1, k.x);
                    k2 = new Koords(k.y - 2, k.x);
                    k3 = new Koords(k.y - 3, k.x);
                    break;
                case 1: //DOWN
                    k1 = new Koords(k.y + 1, k.x);
                    k2 = new Koords(k.y + 2, k.x);
                    k3 = new Koords(k.y + 3, k.x);
                    break;
                case 2://RIGHT
                    k1 = new Koords(k.y, k.x + 1);
                    k2 = new Koords(k.y, k.x + 2);
                    k3 = new Koords(k.y, k.x + 3);
                    break;
                case 3://LEFT
                    k1 = new Koords(k.y, k.x - 1);
                    k2 = new Koords(k.y, k.x - 2);
                    k3 = new Koords(k.y, k.x - 3);
                    break;
                default:
                    System.err.println("pretty much not going to happen");
            }

            if (validKoords(k1)) {
                boolean blocked = false;
                if (specificMap[k1.y][k1.x].what == Square.NODE) {
                    result.add(specificMap[k1.y][k1.x].node);
                }
                if (specificMap[k1.y][k1.x].what == Square.OBSTACLE)
                    blocked = true;

                if (validKoords(k2) && !blocked) {
                    if (specificMap[k2.y][k2.x].what == Square.NODE) {
                        result.add(specificMap[k2.y][k2.x].node);
                    }
                    if (specificMap[k2.y][k2.x].what == Square.OBSTACLE)
                        blocked = true;

                    if (validKoords(k3) && !blocked) {
                        if (specificMap[k3.y][k3.x].what == Square.NODE) {
                            result.add(specificMap[k3.y][k3.x].node);
                        }
                    }
                }
            }
            index++;
        }
        return result;
    }

    //returns true if the coordinates are valid = accessible on the map
    public static boolean validKoords(Koords k) {
        return !(k.y < 0 || k.y >= height || k.x < 0 || k.x >= width);
    }

    public static boolean isObstacle(Koords k) {
        return rootMap[k.y][k.x].what == Square.OBSTACLE;
    }

}

class Square {

    static int EMPTY = 0;
    static int NODE = 1;
    static int OBSTACLE = 2;

    Node node = null;

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
        if (getClass() != o.getClass())
            return false;
        Koords k = (Koords) o;
        return (this.y == k.y && this.x == k.x);
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

class Map {
    Square[][][] allMaps;
    List<Node> nodes = new ArrayList<>();

    public Map(Square[][] rootMap, int rounds) {
        allMaps = new Square[rounds][][];
        allMaps[0] = rootMap;
    }

    //round = last round of read data
    public void predict(int round) {
        for (int y = 0; y < Player.height; y++) {
            for (int x = 0; x < Player.width; x++) {
                if (allMaps[0][y][x].what == Square.NODE) {
                    Node.Direction hypothesis = Node.Direction.NONE;
                    boolean directionFound = false;
                    int index = 0;
                    while (!directionFound) {
                        //the order of the hypothesis kinda matters. if its different several tests don't work
                        switch (index) {
                            case 0:
                                hypothesis = Node.Direction.NONE;
                                break;
                            case 1:
                                hypothesis = Node.Direction.RIGHT;
                                break;
                            case 2:
                                hypothesis = Node.Direction.LEFT;
                                break;
                            case 3:
                                hypothesis = Node.Direction.DOWN;
                                break;
                            case 4:
                                hypothesis = Node.Direction.UP;
                                break;
                            default:
                                System.err.println("No hypothesis fits");
                                break;
                        }

                        Node node = new Node(new Koords(y, x), hypothesis);

                        //predict koords of the node for round 1 to r
                        Koords[] predictedKoords = new Koords[round];
                        for (int r = 0; r < round; r++) {
                            predictedKoords[r] = node.positionAtRound(r + 1);
                        }

                        //check predictions with the real data of maps in round 1 to r
                        directionFound = true;
                        for (int r = 1; r <= round; r++) {
                            Koords predicted = predictedKoords[r - 1];
                            if (allMaps[r][predicted.y][predicted.x].what != Square.NODE) {
                                directionFound = false;
                                break;
                            }
                        }
                        if (directionFound) {
                            nodes.add(node);
                            allMaps[0][y][x].node = node;
                        }

                        index++;
                    }
                }
            }
        }

        //now calculate the remaining maps
        //first add empty maps
        for (int i = round; i < allMaps.length; i++) {
            allMaps[i] = Player.copyMapWithoutNodes(allMaps[0]);
        }

        //then add node by node to the maps
        for (Node n : nodes) {
            n.addToMaps(allMaps);
        }
    }

    public boolean compareMap(int round, Square[][] correct) {

        for (int y = 0; y < Player.height; y++) {
            for (int x = 0; x < Player.width; x++) {
                if (allMaps[round][y][x].what != correct[y][x].what)
                    return false;
            }
        }

        return true;
    }

    public void printMap(int round) {
        for (Square[] asd : allMaps[round]) {
            String out = "";
            for (Square sq : asd) {
                if (sq.what == Square.EMPTY)
                    out += ".";
                else if (sq.what == Square.NODE)
                    out += "@";
                else if (sq.what == Square.OBSTACLE)
                    out += "#";
            }
            System.err.println(out);
        }
    }
}

class Node implements Comparable<Node> {
    private static int idCounter = 0;

    enum Direction {
        NONE, UP, DOWN, LEFT, RIGHT
    }

    Koords start;
    Direction direction;
    int id;

    public Node(Koords s, Direction d) {
        start = s;
        direction = d;
        id = idCounter++;
    }

    @Override
    public boolean equals(Object o) {
        if (getClass() != o.getClass())
            return false;

        Node n = (Node) o;
        return start.equals(n.start);
    }

    @Override
    public int compareTo(Node n) {
        return id - n.id;
    }

    //copy pasta from positionAtRound, changes should probably be applied to both
    public void addToMaps(Square[][][] allMaps) {
        Direction dir = direction;
        Koords currentPlace = start;
        for (int i = 1; i < allMaps.length; i++) {

            boolean nextPosFound = false;
            while (!nextPosFound) {

                Koords next = null;
                switch (dir) {
                    case NONE:
                        next = currentPlace;
                        break;
                    case UP:
                        next = new Koords(currentPlace.y - 1, currentPlace.x);
                        break;
                    case DOWN:
                        next = new Koords(currentPlace.y + 1, currentPlace.x);
                        break;
                    case LEFT:
                        next = new Koords(currentPlace.y, currentPlace.x - 1);
                        break;
                    case RIGHT:
                        next = new Koords(currentPlace.y, currentPlace.x + 1);
                        break;
                    default:
                        System.err.println("Jet fuel can't melt st00l beams: " + dir);
                }

                if (!Player.validKoords(next) || Player.isObstacle(next)) {

                    //change direction and try again
                    if (dir == Direction.UP)
                        dir = Direction.DOWN;
                    else if (dir == Direction.DOWN)
                        dir = Direction.UP;
                    else if (dir == Direction.LEFT)
                        dir = Direction.RIGHT;
                    else if (dir == Direction.RIGHT)
                        dir = Direction.LEFT;
                    else {
                        //happens if direction is NONE
                        System.err.println("Error code 9002");
                    }

                } else {
                    currentPlace = next;
                    allMaps[i][next.y][next.x].what = Square.NODE;
                    allMaps[i][next.y][next.x].node = this;
                    nextPosFound = true;
                }
            }
        }

    }

    public Koords positionAtRound(int round) {
        Direction dir = direction;
        Koords currentPlace = start;
        for (int i = 1; i <= round; i++) {

            boolean nextPosFound = false;
            boolean changedDirection = false;
            while (!nextPosFound) {

                Koords next = null;
                switch (dir) {
                    case NONE:
                        next = currentPlace;
                        break;
                    case UP:
                        next = new Koords(currentPlace.y - 1, currentPlace.x);
                        break;
                    case DOWN:
                        next = new Koords(currentPlace.y + 1, currentPlace.x);
                        break;
                    case LEFT:
                        next = new Koords(currentPlace.y, currentPlace.x - 1);
                        break;
                    case RIGHT:
                        next = new Koords(currentPlace.y, currentPlace.x + 1);
                        break;
                    default:
                        System.err.println("Jet fuel can't melt steel beams: " + dir);
                }

                if (!Player.validKoords(next) || Player.isObstacle(next)) {
                    //if were coming in here a second time that means the node is sandwiched and cannot have a direction
                    if (changedDirection) {
                        dir = Direction.NONE;
                    }

                    changedDirection = true;
                    //change direction and try again
                    if (dir == Direction.UP)
                        dir = Direction.DOWN;
                    else if (dir == Direction.DOWN)
                        dir = Direction.UP;
                    else if (dir == Direction.LEFT)
                        dir = Direction.RIGHT;
                    else if (dir == Direction.RIGHT)
                        dir = Direction.LEFT;

                } else {
                    currentPlace = next;
                    nextPosFound = true;
                }
            }
        }

        return currentPlace;
    }
}

class Detonation

{
    int round;
    Koords koords;
    List<Node> nodes = new ArrayList<>();

    public Detonation(int r, Koords k, List<Node> n) {
        round = r;
        koords = k;

        nodes.addAll(n);
        Collections.sort(nodes);
    }

    public boolean isSubsetOf(Detonation d) {

        //same class
        if (detonatesSameNodes(d))
            return false;

        for (Node n : nodes) {
            if (!d.nodes.contains(n))
                return false;
        }

        return true;
    }

    public boolean detonatesSameNodes(Detonation d) {
        if (nodes.size() != d.nodes.size())
            return false;

        for (int i = 0; i < nodes.size(); i++) {
            if (!nodes.get(i).equals(d.nodes.get(i)))
                return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String out = "";
        out += koords;
        out += " " + round;
        for (Node n : nodes) {
            out += " Node_" + n.id;

        }
        return out;
    }
}