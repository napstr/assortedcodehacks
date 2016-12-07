package hackerrank.java.datastructures.javapriorityqueue;

/**
 * Created by npstr on 07.12.2016
 */

import java.util.PriorityQueue;
import java.util.Scanner;


public class Solution {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int totalEvents = Integer.parseInt(in.nextLine());
        PriorityQueue<Student> pq = new PriorityQueue<>();
        while (totalEvents > 0) {
            String event = in.next();
            if ("ENTER".equals(event)) {
                String name = in.next();
                double cgpa = in.nextDouble();
                int token = in.nextInt();
                pq.add(new Student(token, name, cgpa));
            } else if ("SERVED".equals(event)) {
                pq.poll();
            }
            totalEvents--;
        }

        if (pq.isEmpty()) System.out.println("EMPTY");
        else {
            while (!pq.isEmpty()) {
                System.out.println(pq.remove().getFname());
            }
        }
    }
}

class Student implements Comparable<Student> {
    private int token;
    private String fname;
    private double cgpa;

    public Student(int id, String fname, double cgpa) {
        super();
        this.token = id;
        this.fname = fname;
        this.cgpa = cgpa;
    }

    public int getToken() {
        return token;
    }

    public String getFname() {
        return fname;
    }

    public double getCgpa() {
        return cgpa;
    }

    @Override
    public int compareTo(Student o) {
        if (this.cgpa - o.cgpa > 0) return -1;
        else if (this.cgpa - o.cgpa < 0) return 1;
        else {
            if (this.fname.equals(o.fname)) {
                return this.token - o.token;
            } else return this.fname.compareTo(o.fname);
        }

    }
}