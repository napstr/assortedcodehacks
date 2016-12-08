package hackerrank.java.advanced.lambdaexpressions;

/**
 * Created by npstr on 08.12.2016
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

interface PerformOperation {
    boolean check(int a);
}

class MyMath {
    public static boolean checker(PerformOperation p, int num) {
        return p.check(num);
    }

    PerformOperation is_odd() {
        return (x) -> {
            return x % 2 == 1;
        };
    }

    PerformOperation is_prime() {
        return (x) -> {
            return java.math.BigInteger.valueOf(x).isProbablePrime(1);
        };
    }

    PerformOperation is_palindrome() {
        return (x) -> {
            String number = x + "";
            while (number.length() > 1) {
                char a = number.charAt(0);
                char b = number.charAt(number.length() - 1);
                if (a != b) return false;
                number = number.substring(1, number.length() - 2);
            }
            return true;
        };
    }
}

public class Solution {

    public static void main(String[] args) throws IOException {
        MyMath ob = new MyMath();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());
        PerformOperation op;
        boolean ret = false;
        String ans = null;
        while (T-- > 0) {
            String s = br.readLine().trim();
            StringTokenizer st = new StringTokenizer(s);
            int ch = Integer.parseInt(st.nextToken());
            int num = Integer.parseInt(st.nextToken());
            if (ch == 1) {
                op = ob.is_odd();
                ret = ob.checker(op, num);
                ans = (ret) ? "ODD" : "EVEN";
            } else if (ch == 2) {
                op = ob.is_prime();
                ret = ob.checker(op, num);
                ans = (ret) ? "PRIME" : "COMPOSITE";
            } else if (ch == 3) {
                op = ob.is_palindrome();
                ret = ob.checker(op, num);
                ans = (ret) ? "PALINDROME" : "NOT PALINDROME";

            }
            System.out.println(ans);
        }
    }
}