package dev.paw.uniformity.utils;

import net.minecraft.client.MinecraftClient;

@SuppressWarnings("unused")
public class Number {
    public static int randInt(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static double round(double val, int places) {
        return Math.round(val * Math.pow(10, places)) / Math.pow(10, places);
    }

    public static class Centered {
        private final int width;

        private Centered(int width) {
            this.width = width;
        }

        public int scaledWidth() {
            return MinecraftClient.getInstance().getWindow().getScaledWidth();
        }

        public int x1() {
            return (scaledWidth() / 2) - (width / 2);
        }

        public int x2() {
            return (scaledWidth() / 2) + (width / 2);
        }

        public int getWidth() {
            return width;
        }

        public static Centered fromText(String text) {
            return new Centered(MinecraftClient.getInstance().textRenderer.getWidth(text));
        }

        public static Centered fromInt(int width) {
            return new Centered(width);
        }
    }
}
