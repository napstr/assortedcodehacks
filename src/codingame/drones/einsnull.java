package drones;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

class Shell {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int p = in.nextInt(); // number of players in the game (2 to 4 players)
        int myId = in.nextInt(); // ID of your player (0, 1, 2, or 3)
        int d = in.nextInt(); // number of drones in each team (3 to 11)
        int z = in.nextInt(); // number of zones on the map (4 to 8)

        Game game = new Game(p, d, z);

        for (int i = 0; i < z; i++) {
            int x = in.nextInt(); // corresponds to the position of the center of a zone. A zone is a circle with a radius of 100 units.
            int y = in.nextInt();
            game.zones[i] = new Zone(x, y);
        }

        for (int i = 0; i < p; i++) {
            game.players[i] = new Player(i, d);
            for (int j = 0; j < d; j++) {
                game.players[i].drones[j] = new Drone(i);
            }
        }





        // game loop
        while (true) {
            for (int i = 0; i < z; i++) {
                int tid = in.nextInt(); // ID of the team controlling the zone (0, 1, 2, or 3) or -1 if it is not controlled. The zones are given in the same order as in the initialization.
                game.zones[i].currentOwner = tid;
            }
            for (int i = 0; i < p; i++) {
                for (int j = 0; j < d; j++) {
                    int dx = in.nextInt(); // The first D lines contain the coordinates of drones of a player with the ID 0, the following D lines those of the drones of player 1, and thus it continues until the last player.
                    int dy = in.nextInt();
                    game.players[i].drones[j].x = dx;
                    game.players[i].drones[j].y = dy;
                }
            }

            //find the next drone for every zone
            for (int i = 0; i < z; i++) {
                Drone bestOne = null;
                double bestDistance = Integer.MAX_VALUE;
                Set<Integer> haveDestination = new HashSet<Integer>();
                for (int j = 0; j < d; j++) {
                    if (haveDestination.contains(j)) continue;//ignore drones that already have a destination
                    double distance = calcDistance(game.zones[i].x, game.zones[i].y, game.players[myId].drones[j].x, game.players[myId].drones[j].y);
                    if (distance < bestDistance) {
                        bestDistance = distance;
                        bestOne = game.players[myId].drones[j];
                    }
                }
                bestOne.destX = game.zones[i].x;
                bestOne.destY = game.zones[i].y;

            }


            for (int i = 0; i < d; i++) {

                // Write an action using System.out.println()
                // To debug: System.err.println("Debug messages...");
                String destination = game.players[myId].drones[i].destX + " " + game.players[myId].drones[i].destY;
                System.out.println(destination); // output a destination point to be reached by one of your drones. The first line corresponds to the first of your drones that you were provided as input, the next to the second, etc.
            }
        }
    }

    private static double calcDistance(int aX, int aY, int bX, int bY) {
        double xDiff = aX - bX;
        if (xDiff < 0) xDiff *= -1;

        double yDiff = aY - bY;
        if (yDiff < 0) yDiff *= -1;

        return (Math.sqrt((xDiff * xDiff) + (yDiff * yDiff)));
    }
}

class Player {
    int id;

    Drone[] drones;

    //game expects the main function to sit in the player class for whatever reason
    public static void main(String args[]) {
        Shell.main(args);
    }

    public Player(int i, int d) {
        this.id = i;
        drones = new Drone[d];
    }
}

class Game {
    
    int playersNr;
    
    int dronesNr;

    int zonesNr;

    Player[] players;
    
    Zone[] zones;
    

    public Game(int p, int d, int z) {
        this.playersNr = p;
        this.dronesNr = d;
        this.zonesNr = z;

        players = new Player[playersNr];
        zones = new Zone[zonesNr];
    }
    
}

class Zone {
    int x;
    
    int y;
    
    int currentOwner = -1; //playerID

    public Zone(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

class Drone {
    int x;
    
    int y;

    int destX = 2000;//middle of the field

    int destY = 900;//middle of the field
    
    int owner; //playerID

    public Drone(int o) {
        this.owner = o;
    }
}