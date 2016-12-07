package hackerrank.java.introduction.javadateandtime;

/**
 * Created by npstr on 06.12.2016
 */

import java.util.Calendar;
import java.util.Locale;
import java.util.Scanner;

public class Solution {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int month = in.nextInt();
        int day = in.nextInt();
        int year = in.nextInt();

        Calendar c = (new Calendar.Builder()).set(Calendar.DAY_OF_MONTH, day).set(Calendar.YEAR, year).set(Calendar.MONTH, month - 1).build();
        String asd = c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH);
        System.out.println(asd.toUpperCase());
    }
}
