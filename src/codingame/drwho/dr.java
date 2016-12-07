package drwho;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Solution {

    private int width;
    private int height;
    private String rawData;
    private boolean[][] image;
    private List<Line> lines = new ArrayList<>();
    private List<Tail> tails = new ArrayList<>();
    private final static boolean DEBUG = false;

    public static void main(String args[]) throws FileNotFoundException {
        Scanner in = new Scanner(System.in);
        //Scanner in = new Scanner(new File("src\\codingame\\drwho\\10.txt"));
        Solution solution = new Solution();
        solution.width = in.nextInt();
        solution.height = in.nextInt();
        in.nextLine();
        solution.rawData = in.nextLine();

        solution.image = solution.createImage();

        // creates a nice txt file
        if (DEBUG) {
            try {
                PrintWriter writer = new PrintWriter("debug.txt", "UTF-8");
                for (boolean[] bArr : solution.image) {
                    String out = "";
                    for (boolean b : bArr) {
                        out += b ? "+" : " ";
                    }
                    writer.println(out);
                    System.err.println(out);
                }
                writer.close();
            } catch (Exception e) {
                System.err.println("Error during creating the debug.txt file: " + e.getMessage());
            }
        }


        //A: find the 5 lines
        double percent = 0.6; //60% determined by try and fail
        solution.findLines(percent);

        if (DEBUG) {
            System.err.println("Lines:");
            for (Line l : solution.lines) {
                System.err.println("TB: " + l.top + " to " + l.bot);
            }
        }
        //A2: add sixth line
        solution.lines.add(solution.createImaginaryCLine());


        //B: find notes by their tails
        solution.findTails(0.25); //25% determined by try and fail, (e.g. 20% or 30% don't work on all cases)

        if (DEBUG) {
            System.err.println("Tails:");
            for (Tail t : solution.tails) {
                System.err.println("LR: " + t.left + " to " + t.right);
            }
        }


        //C: determine notes & output
        System.out.println(solution.determineNotes().substring(1)); //remove whitespace at the start
    }

    private String determineNotes() {

        String result = "";
        for (Tail t : tails) {
            result += " " + identifyNote(t);
        }
        return result;
    }

    /**
     * Look one left or right and find any black pixels there (except of lines)
     */
    private String identifyNote(Tail t) {
        String result = "";

        //look left
        boolean left = true;
        int x = t.left - 1;
        List<Integer> allBlackLeftNoLines = removeLineBlacks(getVerticalBlacks(x));

        //if there's nothing, look right
        if (allBlackLeftNoLines.isEmpty()) {
            left = false;
            x = t.right + 1;
            allBlackLeftNoLines = removeLineBlacks(getVerticalBlacks(x));
        }

        //THE LOGIC 2.0
        //get all black pixels that are not part of lines to the left/right
        //find their mid and determine note & half/quarter with its help

        int xSum = 0;
        int ySum = 0;
        int pixels = 0;

        do {
            for (Integer i : allBlackLeftNoLines) {
                xSum += x;
                ySum += i;
                pixels++;
            }

            if (left) {
                x--;
            } else {
                x++;
            }
            allBlackLeftNoLines = removeLineBlacks(getVerticalBlacks(x));

        } while (!allBlackLeftNoLines.isEmpty());

        int xCenter = xSum / pixels;
        int yCenter = ySum / pixels;

        if (DEBUG) {
            System.err.println("Center at " + yCenter + " | " + xCenter);
        }

        result += getNoteByCenter(yCenter);

        //if the note is black at the center is a quart, else its half
        if (image[yCenter][xCenter]) {
            result += "Q";
        } else {
            result += "H";
        }

        return result;
    }

    private String getNoteByCenter(int y) {
        Line l1 = lines.get(0);
        Line l2 = lines.get(1);
        Line l3 = lines.get(2);
        Line l4 = lines.get(3);
        Line l5 = lines.get(4);
        Line l6 = lines.get(5);

        if (y < l1.top) {
            return "G";
        } else if (y >= l1.top && y <= l1.bot) {
            return "F";
        } else if (y > l1.bot && y < l2.top) {
            return "E";
        } else if (y >= l2.top && y <= l2.bot) {
            return "D";
        } else if (y > l2.bot && y < l3.top) {
            return "C";
        } else if (y >= l3.top && y <= l3.bot) {
            return "B";
        } else if (y > l3.bot && y < l4.top) {
            return "A";
        } else if (y >= l4.top && y <= l4.bot) {
            return "G";
        } else if (y > l4.bot && y < l5.top) {
            return "F";
        } else if (y >= l5.top && y <= l5.bot) {
            return "E";
        } else if (y > l5.bot && y < l6.top) {
            return "D";
        } else if (y >= l6.top) {
            return "C";
        }

        return "OMGWTFBBQ";//obviously, if this happens something is horribly wrong here and justifies returning this code
    }

    private Line createImaginaryCLine() {
        Line l1 = lines.get(0);
        Line l2 = lines.get(1);
        Line l3 = lines.get(2);
        Line l4 = lines.get(3);
        Line l5 = lines.get(4);

        int topSum = (l5.top - l4.top) + (l4.top - l3.top) + (l3.top - l2.top) + (l2.top - l1.top);
        int botSum = (l5.bot - l4.bot) + (l4.bot - l3.bot) + (l3.bot - l2.bot) + (l2.bot - l1.bot);

        int averageSpacing = ((topSum / 4) + (botSum / 4)) / 2;

        int imaginaryTop = l5.top + averageSpacing;
        int imaginaryBot = l5.bot + averageSpacing;

        if (DEBUG) System.err.println("CLine: " + imaginaryTop + " to " + imaginaryBot);

        return new Line(imaginaryTop, imaginaryBot);
    }

    private List<Integer> removeLineBlacks(List<Integer> blacks) {
        List<Integer> result = new ArrayList<>(blacks);

        for (Line l : lines) {
            for (int i = l.top; i <= l.bot; i++) {
                result.remove(new Integer(i));
            }
        }

        return result;
    }

    private List<Integer> getVerticalBlacks(int x) {
        List<Integer> result = new ArrayList<>();

        for (int y = 0; y < height; y++) {
            if (image[y][x]) {
                result.add(y);
            }
        }
        return result;
    }

    /**
     * Looks for tails of the notes
     * <p>
     * criteria: X% of a line is a consecutive line of black
     * pretty much copypasta from findLines()
     */
    private void findTails(double percent) {
        boolean lfLeft = true;
        int leftEnd = -1;
        int rightEnd;

        for (int x = 0; x < width; x++) {

            //find start of a line
            if (lfLeft) {
                int bCount = howManyBlacksVertical(x);
                if (bCount >= height * percent) {
                    leftEnd = x;
                    lfLeft = false;
                }
                continue;
            }

            //then find the end
            int bCount = howManyBlacksVertical(x);
            if (bCount < height * percent) {
                rightEnd = x - 1;
                //save the middle
                tails.add(new Tail(leftEnd, rightEnd));
                lfLeft = true;
            }
        }
    }

    /**
     * Looks for the five lines
     * <p>
     * criteria: X% of a line is black
     */
    private void findLines(double percent) {

        boolean lfUpper = true;
        int upperEnd = -1;
        int lowerEnd;

        for (int y = 0; y < height; y++) {
            //find start of a line
            if (lfUpper) {
                int bCount = howManyBlacks(image[y]);
                if (bCount >= width * percent) {
                    upperEnd = y;
                    lfUpper = false;
                }
                continue;
            }

            //then find the end
            int bCount = howManyBlacks(image[y]);
            if (bCount < width * percent) {
                lowerEnd = y - 1;
                //save the middle
                lines.add(new Line(upperEnd, lowerEnd));
                lfUpper = true;
            }
        }
    }


    private int howManyBlacksVertical(int x) {
        int result = 0;
        for (int y = 0; y < height; y++) {
            boolean b = image[y][x];
            if (b) result++;
        }

        return result;
    }

    private int howManyBlacks(boolean[] line) {
        int result = 0;

        for (boolean b : line) {
            if (b) result++;
        }

        return result;
    }

    private boolean[][] createImage() {
        boolean[][] result = new boolean[height][];

        for (int i = 0; i < height; i++) result[i] = new boolean[width];

        String[] separatedData = rawData.split(" ");

        int index = -1;
        boolean black = true;
        int blocksToAdd = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (blocksToAdd <= 0) {
                    black = separatedData[++index].equals("B");
                    blocksToAdd = Integer.valueOf(separatedData[++index]);
                }
                result[y][x] = black;
                blocksToAdd--;
            }
        }

        return result;
    }

}

class Tail {
    //inclusive
    int left;
    int right;
    double mid;

    public Tail(int l, int r) {
        left = l;
        right = r;
        mid = (l + r) / 2.0;
    }
}

class Line {
    //inclusive
    int top;
    int bot;
    double mid;

    public Line(int t, int b) {
        top = t;
        bot = b;
        mid = (t + b) / 2.0;
    }
}

///**
// * This is dirty....but we really really need it as a information container ._.
// */
//class NoteData {
//    int onLine;
//    String note;
//
//    NoteData(int o, String n) {
//        onLine = o;
//        note = n;
//    }
//}