package hackerrank.java.OOP.javamethodoverriding2superkeyword;

/**
 * Created by npstr on 07.12.2016
 */

class BiCycle {
    String define_me() {
        return "a vehicle with pedals.";
    }
}

class MotorCycle extends BiCycle {
    String define_me() {
        return "a cycle with an engine.";
    }

    MotorCycle() {
        System.out.println("Hello I am a motorcycle, I am " + define_me());

        String temp = super.define_me(); //Fix this line

        System.out.println("My ancestor is a cycle who is " + temp);
    }

}

class Solution {
    public static void main(String[] args) {
        MotorCycle M = new MotorCycle();
    }
}
