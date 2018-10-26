package com.wolfman.java.io;

public class StaticA {

    public StaticA(){
        System.out.println("我是A");
    }

    {
        System.out.println("a");
    }

    static {
        System.out.println("static A");
    }

    public static void main(String[] args) {
        new StaticB();
        new StaticB();
    }

}

class StaticB extends StaticA {

    public StaticB(){
        System.out.println("我是B");
    }

    {
        System.out.println("b");
    }

    static {
        System.out.println("static B");
    }

}
