package dev.paw.uniformity.utils;

public class Timer {

    private long startTime;

    public Timer() {
        startTime = getCurrentTime();
    }

    private long getCurrentTime() {
        return System.currentTimeMillis();
    }

    public void reset() {
        this.startTime = getCurrentTime();
    }

    public boolean hasElapsed(long ms) {
        return getElapsed()  >= ms;
    }

    public long getElapsed() {
        return getCurrentTime() - this.startTime;
    }

    public String getTimeStr() {
        long delta = getElapsed() / 1000;
        int hours = (int) (delta / 3600);
        int minutes = (int) ((delta % 3600) / 60);
        int seconds = (int) (delta % 60);
        String time = "";
        if (hours > 0) time += hours + "h";
        if (minutes > 0) time += minutes + "m";
        if (seconds > 0) time += seconds + "s";
        return time;
    }
}
