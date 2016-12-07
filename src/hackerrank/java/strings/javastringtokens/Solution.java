package hackerrank.stringtoken;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Solution {


    public static void main(String[] args) {
        Scanner in;
        try {
            in = new Scanner(new File("R:\\Dropbox\\Programmieren\\einsnull\\src\\hackerrank\\javastringtokens\\test"));
        } catch (FileNotFoundException e) {
            in = new Scanner(System.in);
        }
        String s = in.nextLine();
        String regex = "[!,?._\'@\\s]+";
        s = s.replaceAll(regex, " ");
        String[] tmp = s.split(" ");
        List<String> result = new ArrayList<>();
        for (String str : tmp) {
            if (str.length() > 0) result.add(str);
        }
        System.out.println(result.size());
        for (String r : result)
            System.out.println(r);
    }
}
