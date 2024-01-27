package dev.paw.uniformity.utils;

public class Number {
    public static int randInt(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

}
