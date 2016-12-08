package hackerrank.java.advanced.javasingletonpattern;

/**
 * Created by npstr on 07.12.2016
 */

class Singleton {

    public String str;

    private static Singleton s = new Singleton();

    private Singleton() {

    }

    static Singleton getSingleInstance() {
        return s;
    }
}
