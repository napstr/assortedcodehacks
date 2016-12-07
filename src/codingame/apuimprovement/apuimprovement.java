package apuimprovement;

import java.io.FileNotFoundException;
import java.util.*;

class Player {

    private static Node[][] globalMap;
    private static final Set<Conn> GLOBAL_CONNs = new HashSet<>();
    private static final Set<Node> globalNodes = new HashSet<>();
    private static final List<String> globalOut = new ArrayList<>();

    //save references to all nodes, plz dont remove any
    private static final Set<Node> allNodes = new HashSet<>();

    public static void main(String args[]) throws FileNotFoundException {
        Scanner in = new Scanner(System.in);
        //		in = new Scanner(new File("src\\apuimprovement\\ms2"));

        int width = in.nextInt(); // the number of cells on the X axis
        System.err.println(width);
        in.nextLine();

        int height = in.nextInt(); // the number of cells on the Y axis
        System.err.println(height);
        in.nextLine();

        globalMap = new Node[height][width];

        for (int y = 0; y < height; y++) {
            String line = in.nextLine(); // width characters, each either a number or a '.'
            System.err.println(line);
            for (int x = 0; x < width; x++) {
                char amount = line.charAt(x);

                if (amount == '.') {
                    globalMap[y][x] = null;//do nothing
                } else {
                    Node n = new Node(x, y, Character.getNumericValue(amount));
                    globalNodes.add(n);
                    allNodes.add(n);
                    globalMap[y][x] = n;
                }
            }
        }

        buildNetwork(globalMap, GLOBAL_CONNs);//creates all connections

        boolean areWeDoneYet = false;

        while (!areWeDoneYet) {

            boolean built = buildBridges(globalMap, GLOBAL_CONNs, globalNodes, globalOut);

            if (!built) {
                reduceMap(globalMap, GLOBAL_CONNs, globalNodes);
                //if map empty
                if (globalNodes.isEmpty())
                    break; //we are done

                //may as well output what we got so far
                globalOut.forEach(System.out::println);
                globalOut.clear();

                if (!goBogo(globalMap, GLOBAL_CONNs, globalNodes, globalOut)) {
                    globalOut.add("bogo has failed us T_T");
                    break;
                }
            }

        }

        globalOut.forEach(System.out::println);
    }

    private static boolean goBogo(Node[][] map, Set<Conn> allConns, Set<Node> allNodes, List<String> out) {

        //save current state
        Node[][] savedMap = copyMap(map);
        Set<Conn> savedConns = new HashSet<>(allConns);
        Set<Node> savedNodes = new HashSet<>(allNodes);
        List<String> savedOut = new ArrayList<>(out);

        //when bogo fucks up, restore state & try other connection

        List<Conn> possibleBridges = bogoBridges(map, allConns);

        //lets go full random on this one
        while (possibleBridges.size() > 0) {
            int bogo = (int) Math.random() * possibleBridges.size();
            Conn c = possibleBridges.remove(bogo);

            addBridgesToConn(1, c, allConns, out);

            if (businessAsUsual(map, allConns, allNodes, out)) {
                //we're done
                return true;
            }

            //else reset for next run
            map = copyMap(savedMap);
            allConns = new HashSet<>(savedConns);
            allNodes = new HashSet<>(savedNodes);
            out = new ArrayList<>(savedOut);
        }

        //if we get here this bogo has failed us
        return false;
    }

    private static boolean businessAsUsual(Node[][] map, Set<Conn> allConns, Set<Node> allNodes, List<String> out) {
        while (true) {

            boolean built = buildBridges(map, allConns, allNodes, out);

            if (!built) {
                reduceMap(map, allConns, allNodes);
                //if map empty
                if (allNodes.isEmpty()) {
                    return true;
                }

                //if map is fucked up, revert to last bogo
                if (isMapFuckedUp()) {
                    return false;
                }

                //no clear move possible, but it isn't done yet and it's not fucked -> go bogo another time!
                return goBogo(map, allConns, allNodes, out);
            }
        }
    }

    private static boolean isMapFuckedUp() {
        // island detected?

        List<Node> nodes = new ArrayList<>(allNodes);

        boolean island = true;

        while (nodes.size() > 0) {

            Node n = nodes.remove(0);
            Set<Node> connected = gimmeConnectedNodes(n);

            nodes.removeAll(connected);

            for (Node node : connected) {
                if (node.currentBridges < node.maxBridges)
                    island = false;
            }

        }

        return island;
    }

    //returns all connected nodes to node n, which could form an island
    private static Set<Node> gimmeConnectedNodes(Node n) {

        Set<Node> result = new HashSet<>();

        List<Node> buffer = new ArrayList<>();
        buffer.add(n);

        while (buffer.size() > 0) {
            Node current = buffer.remove(0);

            if (result.contains(current))
                continue;

            result.add(current);

            for (Conn c : current.getConns()) {
                Node other = c.getOtherNode(current);
                buffer.add(other);
            }
        }

        return result;
    }

    private static Node[][] copyMap(Node[][] map) {
        Node[][] result = new Node[map.length][];

        for (int y = 0; y < map.length; y++) {
            result[y] = new Node[map[y].length];
            for (int x = 0; x < map[y].length; x++) {
                result[y][x] = map[y][x];
            }
        }
        return result;
    }

    private static boolean reduceMap(Node[][] map, Set<Conn> allConns, Set<Node> allNodes) {
        boolean result = false;
        //remove all nodes that have reached maxBridges
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                if (map[y][x] != null) {
                    Node n = map[y][x];
                    if (n.currentBridges == n.maxBridges) {
                        result = true;
                        for (Conn c : n.getConns()) {
                            c.getOtherNode(n).currentBridges -= c.bridges;
                            c.getOtherNode(n).maxBridges -= c.bridges;
                            c.getOtherNode(n).getConns().remove(c);
                            allConns.remove(c);
                        }
                        allNodes.remove(n);
                        map[y][x] = null;
                    }
                }
            }
        }
        return result;
    }

    private static List<Conn> bogoBridges(Node[][] map, Set<Conn> allConns) {
        System.err.println("Going BOGO");
        List<Conn> result = new ArrayList<>();

        for (Conn c : allConns) {
            if (openSlots(c) > 0) {
                result.add(c);
            }
        }

        //		for (Node[] line : map)
        //		{
        //			for (Node n : line)
        //			{
        //				if (n != null)
        //				{
        //					if (n.currentBridges < n.maxBridges)
        //					{
        //						for (Conn c : n.getConns())
        //						{
        //							if (openSlots(c) > 0)
        //							{
        //								Node other = c.getOtherNode(n);
        //								if (other.currentBridges < other.maxBridges)
        //								{
        //									System.err.println(n + "current Bridges: " + n.currentBridges);
        //									if (!performIslandCheck(c))
        //									{
        //										addBridgesToConn(1, c);
        //										System.err.println("BOGO adds one bridge to " + c);
        //										return true;
        //									}
        //								}
        //							}
        //						}
        //					}
        //				}
        //			}
        //		}

        return result;
    }

    private static boolean buildBridges(Node[][] map, Set<Conn> allConns, Set<Node> allNodes, List<String> out) {

        boolean result = false;
        for (Node[] line : map) {
            for (Node n : line) {
                if (n != null) {
                    //find open slots
                    int slotsOpen = 0;

                    for (Conn c : n.getConns()) {
                        slotsOpen += openSlots(c);
                    }

                    int bridgesStillNeeded = n.maxBridges - n.currentBridges;

                    //System.err.println("Node " + n + ", " + bridgesStillNeeded + " bridges needed, " + slotsOpen + " open slots on " + n.getConns().size() + " connections");

                    if (bridgesStillNeeded == slotsOpen && slotsOpen > 0) {
                        System.err.println("Action: fill all open slots");
                        //all conns get all possible bridges
                        for (Conn c : n.getConns()) {
                            int possibleBridges = openSlots(c);
                            if (!performIslandCheck(c, allNodes)) {
                                if (addBridgesToConn(possibleBridges, c, allConns, out))
                                    result = true;
                            }
                        }

                    } else if (bridgesStillNeeded + 1 == slotsOpen && slotsOpen > 2) {
                        System.err.println("Action: build 1 bridge on all connections that have 2 open slots");
                        //all conns that allow 2 bridges get 1 bridge
                        for (Conn c : n.getConns()) {
                            int possibleBridges = openSlots(c);
                            if (possibleBridges == 2) {
                                if (!performIslandCheck(c, allNodes)) {
                                    if (addBridgesToConn(1, c, allConns, out))
                                        result = true;
                                }
                            }
                        }
                    } else if (n.getConns().size() == 1) {
                        Conn c = n.getConns().iterator().next();
                        System.err.println("Action: node " + n + " has only one connection " + c + " , fill it with maximum bridges");
                        //if there is only one connection, all bridges should be built on it
                        if (!performIslandCheck(c, allNodes)) {
                            if (addBridgesToConn(openSlots(c), c, allConns, out))
                                result = true;
                        }

                    }
                }
            }
        }
        return result;
    }

    private static boolean performIslandCheck(Conn c, Set<Node> allNodes) {
        System.err.println("Islandcheck");
        boolean result = false;

        c.bridges++;
        c.node1.currentBridges++;
        c.node2.currentBridges++;

        Set<Node> beenThere = new HashSet<>();

        boolean areAllMaxed = recursiveIslandCheck(c.node1, beenThere);
        if (areAllMaxed && beenThere.size() < allNodes.size()) {
            //island confirmed
            result = true;
        }

        c.bridges--;
        c.node1.currentBridges--;
        c.node2.currentBridges--;
        return result;
    }

    private static boolean recursiveIslandCheck(Node n, Set<Node> beenThere) {
        boolean result = true;
        if (beenThere.contains(n)) {
            return true;
        }

        beenThere.add(n);

        if (n.currentBridges == n.maxBridges) {
            for (Conn c : n.getConns()) {
                if (c.bridges > 0) {
                    if (!recursiveIslandCheck(c.getOtherNode(n), beenThere)) {
                        result = false;
                    }
                }
            }
        } else {
            result = false;
        }

        return result;
    }

    private static void cleanCons(Conn c, Set<Conn> allConns) {

        System.err.println("Cleaning conns");
        //vertical or horizontal?

        Set<Conn> toDelete = new HashSet<>();

        //vertical
        if (c.node1.koords.x == c.node2.koords.x) {

            for (Conn anyConn : allConns) {

                if (anyConn.node1.koords.x == anyConn.node2.koords.x) {
                    continue;
                }

                boolean crossed = false;

                int anySmallX;
                int anyBigX;
                if (anyConn.node1.koords.x < anyConn.node2.koords.x) {
                    anySmallX = anyConn.node1.koords.x;
                    anyBigX = anyConn.node2.koords.x;
                } else {
                    anySmallX = anyConn.node2.koords.x;
                    anyBigX = anyConn.node1.koords.x;
                }

                int smallY;
                int bigY;
                if (c.node1.koords.y < c.node2.koords.y) {
                    smallY = c.node1.koords.y;
                    bigY = c.node2.koords.y;
                } else {
                    smallY = c.node2.koords.y;
                    bigY = c.node1.koords.y;
                }

                if ((anySmallX < c.node1.koords.x && c.node1.koords.x < anyBigX) && (smallY < anyConn.node1.koords.y
                        && anyConn.node1.koords.y < bigY)) {
                    crossed = true;
                }

                if (crossed) {
                    toDelete.add(anyConn);
                }
            }

        } else {//horizontal

            for (Conn anyConn : allConns) {

                if (anyConn.node1.koords.y == anyConn.node2.koords.y) {
                    continue;
                }
                boolean crossed = false;

                int anySmallY;
                int anyBigY;
                if (anyConn.node1.koords.y < anyConn.node2.koords.y) {
                    anySmallY = anyConn.node1.koords.y;
                    anyBigY = anyConn.node2.koords.y;
                } else {
                    anySmallY = anyConn.node2.koords.y;
                    anyBigY = anyConn.node1.koords.y;
                }

                int smallX;
                int bigX;
                if (c.node1.koords.x < c.node2.koords.x) {
                    smallX = c.node1.koords.x;
                    bigX = c.node2.koords.x;
                } else {
                    smallX = c.node2.koords.x;
                    bigX = c.node1.koords.x;
                }

                if ((anySmallY < c.node1.koords.y && c.node1.koords.y < anyBigY) && (smallX < anyConn.node1.koords.x
                        && anyConn.node1.koords.x < bigX)) {
                    crossed = true;
                }

                if (crossed) {
                    toDelete.add(anyConn);
                }
            }

        }

        for (Conn deConn : toDelete) {
            deConn.node1.getConns().remove(deConn);
            deConn.node2.getConns().remove(deConn);
            allConns.remove(deConn);
        }
    }

    private static boolean addBridgesToConn(int b, Conn c, Set<Conn> allConns, List<String> out) {

        if (b == 0)
            return false;

        c.bridges += b;
        c.node1.currentBridges += b;
        c.node2.currentBridges += b;
        System.err.println(c + " gets " + b + " bridges");

        if (c.bridges > 2) {
            System.err.println("FAIL: Bridges at " + c + " too high");
        }
        if (c.node1.currentBridges > c.node1.maxBridges) {
            System.err.println("FAIL: Bridges at " + c.node1 + " too high");
        }
        if (c.node2.currentBridges > c.node2.maxBridges) {
            System.err.println("FAIL: Bridges at " + c.node2 + " too high");
        }

        //build an output string as specified
        String output = "";
        output += c.node1.koords.x + " ";
        output += c.node1.koords.y + " ";
        output += c.node2.koords.x + " ";
        output += c.node2.koords.y + " ";
        output += b;

        out.add(output);

        if (c.bridges == b) { //before was 0 = entirely new bridge created which may block other connections
            cleanCons(c, allConns);
        }
        return true;
    }

    private static int openSlots(Conn c) {
        int result = 2;

        //how many open slots has one node?
        int openSlotsThisNode = c.node1.maxBridges - c.node1.currentBridges;

        //how many open slots has the other node?
        int openSlotsOtherNode = c.node2.maxBridges - c.node2.currentBridges;

        //how many open slots has the connection itself?
        int openSlotsConn = 2 - c.bridges;

        //the smaller of all, (max 2), are the open slots of this connection

        if (openSlotsThisNode < result) {
            result = openSlotsThisNode;
        }

        if (openSlotsOtherNode < result) {
            result = openSlotsOtherNode;
        }

        if (openSlotsConn < result) {
            result = openSlotsConn;
        }

        return result;
    }

    private static void buildNetwork(Node[][] map, Set<Conn> allConns) {
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {

                if (map[y][x] != null) {

                    //look right
                    if (x < map[y].length - 1) {
                        for (int xright = x + 1; xright < map[y].length; xright++) {
                            if (map[y][xright] != null) {
                                Conn conn = new Conn(map[y][x], map[y][xright]);
                                map[y][x].addConn(conn);
                                map[y][xright].addConn(conn);
                                allConns.add(conn);
                                break;
                            }
                        }
                    }

                    //look down

                    if (y < map.length - 1) {
                        for (int ybot = y + 1; ybot < map.length; ybot++) {
                            if (map[ybot][x] != null) {
                                Conn conn = new Conn(map[y][x], map[ybot][x]);
                                map[y][x].addConn(conn);
                                map[ybot][x].addConn(conn);
                                allConns.add(conn);
                                break;
                            }
                        }
                    }
                }

            }
        }
    }
}

class Koords {
    public int x;
    public int y;

    public Koords(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        Koords k = (Koords) o;

        if (this.x != k.x) {
            return false;
        }

        if (this.y != k.y) {
            return false;
        }

        return true;
    }
}

class Node {

    public int maxBridges;

    public int currentBridges = 0;

    public Koords koords;

    private Set<Conn> conns = new HashSet<>();

    public Node(int x, int y, int a) {

        this.maxBridges = a;
        this.koords = new Koords(x, y);
    }

    public Koords getKoords() {
        return koords;
    }

    public void addConn(Conn conn) {
        conns.add(conn);
    }

    public Set<Conn> getConns() {
        return conns;
    }

    @Override
    public boolean equals(Object o) {

        Node n = (Node) o;

        return this.koords.equals(n.koords);
    }

    @Override
    public String toString() {
        return "(" + koords.y + "|" + koords.x + ")";
    }
}

class Conn {

    public Node node1;

    public Node node2;

    public int bridges = 0; //allowed: 0, 1, 2

    public Conn(Node n1, Node n2) {
        this.node1 = n1;
        this.node2 = n2;
        this.bridges = 0;
    }

    public Node getOtherNode(Node n) {
        if (n.equals(node1)) {
            return node2;
        } else {
            return node1;
        }
    }

    @Override
    public boolean equals(Object o) {

        Conn c = (Conn) o;

        if (this.node1.equals(c.node1) && this.node2.equals(c.node2)) {
            return true;
        }

        if (this.node1.equals(c.node2) && this.node2.equals(c.node1)) {
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        return "[" + node1 + " <-> " + node2 + "]";
    }
}