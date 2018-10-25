package com.wolfman.java.multi.thread.MyVolatile;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;

public class ThreadDemo {

    private static volatile  ThreadDemo instance = null;

    public static ThreadDemo getInstance(){
        if (instance == null){
            return  new ThreadDemo();
        }
        return instance;
    }

    public static void main(String[] args) {
        ThreadDemo.getInstance();
    }



}
