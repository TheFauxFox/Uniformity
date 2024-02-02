package dev.paw.uniformity.utils;

public class Number {
    public static int randInt(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static double round(double val, int places) {
        return Math.round(val * Math.pow(10, places)) / Math.pow(10, places);
    }
}
