package hackerrank.java.advanced.javamd5;

/**
 * Created by npstr on 08.12.2016
 */

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class Solution {

    public static void main(String[] args) throws NoSuchAlgorithmException {

        Scanner in = new Scanner(System.in);
        String t = in.nextLine();

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.reset();
        byte[] md5 = md.digest(t.getBytes());
        BigInteger bigInt = new BigInteger(1, md5);
        String hashText = bigInt.toString(16);
        while (hashText.length() < 32) {
            hashText = "0" + hashText;
        }
        System.out.println(hashText);
    }
}