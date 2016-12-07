package hackerrank.java.datastructures.javasort;

/**
 * Created by npstr on 06.12.2016
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

class Student implements Comparable<Student> {
    private int id;
    private String fname;
    private double cgpa;

    public Student(int id, String fname, double cgpa) {
        super();
        this.id = id;
        this.fname = fname;
        this.cgpa = cgpa;
    }

    public int getId() {
        return id;
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
                return this.id - o.id;
            } else return this.fname.compareTo(o.fname);
        }

    }
}

//Complete the code
public class Solution {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int t = in.nextInt();

        List<Student> studentList = new ArrayList<>();
        for (int i = 0; i < t; i++) {
            int id = in.nextInt();
            String fname = in.next();
            double cgpa = in.nextDouble();
            studentList.add(new Student(id, fname, cgpa));
        }

        Collections.sort(studentList);

        for (Student st : studentList) {
            System.out.println(st.getFname());
        }
    }
}
