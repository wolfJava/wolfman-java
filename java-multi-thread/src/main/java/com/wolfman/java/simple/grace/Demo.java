package com.wolfman.java.simple.grace;

public class Demo {

    PrintProcessor printProcessor;

    protected Demo(){

        SaveProcessor saveProcessor=new SaveProcessor();

        saveProcessor.start();

        printProcessor=new PrintProcessor(saveProcessor);

        printProcessor.start();
    }

    private void doTest(Request request){
        printProcessor.processRequest(request);
    }

    public static void main(String[] args) {
        Request request=new Request();
        request.setName("Mic");
        new Demo().doTest(request);
    }

}
