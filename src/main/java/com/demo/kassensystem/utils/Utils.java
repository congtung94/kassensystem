package com.demo.kassensystem.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    public static String getTimeNow(){
        return new SimpleDateFormat("dd/MM/yyyy  HH:mm").format(new Date());
    }

    public static void main(String[] args) {
        System.out.println(getTimeNow());
    }
}
