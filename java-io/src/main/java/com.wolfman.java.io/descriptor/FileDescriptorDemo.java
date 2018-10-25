package com.wolfman.java.io.descriptor;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileDescriptorDemo {

    public static void main(String[] args) throws IOException {

        FileOutputStream out = new FileOutputStream(FileDescriptor.out);
        out.write(new byte[]{97,98,99});
        out.close();

        System.out.print('A');

    }

}
