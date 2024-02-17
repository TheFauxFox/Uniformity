package dev.paw.uniformity.events;

import dev.paw.uniformity.utils.Color;

public class SplashColorEvent extends Event {
    private int color = Color.rgb(239, 50, 61).asInt;

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setColor(Color color) {
        this.color = color.asInt;
    }
}
