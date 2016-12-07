package hackerrank.java.strings.javaregex;

import java.util.Scanner;

class Solution {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            String IP = in.next();
            System.out.println(IP.matches(new myRegex().pattern));
        }

    }
}

class myRegex {
    String pattern = "((25[0-6])|(2[0-4][0-9])|([0-1]?[0-9]?[0-9]?))\\.((25[0-6])|(2[0-4][0-9])|([0-1]?[0-9]?[0-9]?))\\.((25[0-6])|(2[0-4][0-9])|([0-1]?[0-9]?[0-9]?))\\.((25[0-6])|(2[0-4][0-9])|([0-1]?[0-9]?[0-9]?))";
}