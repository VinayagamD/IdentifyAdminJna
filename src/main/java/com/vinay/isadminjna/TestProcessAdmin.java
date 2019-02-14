package com.vinay.isadminjna;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TestProcessAdmin {

    public static void main(String[] args) {
        System.out.println("Is Admin = "+isAdmin());
        System.out.println(System.getProperties());
    }

    public static  boolean isAdmin() {
            StringBuilder outputbuilder = new StringBuilder();
        try {
            ProcessBuilder builder = new ProcessBuilder(
                    "cmd.exe","/c" ,"net user");
            builder.redirectErrorStream(true);
            Process p = builder.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while (true) {
                line = r.readLine();
                if (line == null) { break; }
                outputbuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        System.out.println(outputbuilder.toString());
        return outputbuilder.toString().contains("Administrator");
    }
}
