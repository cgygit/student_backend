package com.example.demo.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

// 用子进程读取输出流
public class InputStreamRunnable extends Thread {
    BufferedReader bReader = null;
    String type = null;
    public InputStreamRunnable(InputStream is, String _type) {
        try {
            bReader = new BufferedReader(new InputStreamReader(new BufferedInputStream(is), "gbk"));
            type = _type;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void run() {
        String line;
        int lineNum = 0;
        try {
            while ((line = bReader.readLine()) != null) {
                lineNum++;
                System.out.println(type+":"+line);
            }
            bReader.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}