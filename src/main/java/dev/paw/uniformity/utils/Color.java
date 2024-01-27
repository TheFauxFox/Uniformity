package dev.paw.uniformity.utils;

public class Color {
    public static Color DARK_RED = Color.rgb(170, 0, 0);
    public static Color RED = Color.rgb(255, 85, 85);
    public static Color GOLD = Color.rgb(255, 170, 0);
    public static Color YELLOW = Color.rgb(255, 255, 85);
    public static Color DARK_GREEN = Color.rgb(0, 170, 0);
    public static Color GREEN = Color.rgb(85, 255, 85);
    public static Color AQUA = Color.rgb(85, 255, 255);
    public static Color DARK_AQUA = Color.rgb(0, 170, 170);
    public static Color DARK_BLUE = Color.rgb(0, 0, 170);
    public static Color BLUE = Color.rgb(85, 85, 255);
    public static Color LIGHT_PURPLE = Color.rgb(255, 85, 255);
    public static Color DARK_PURPLE = Color.rgb(170, 0, 170);
    public static Color WHITE = Color.rgb(255, 255, 255);
    public static Color GRAY = Color.rgb(170, 170, 170);
    public static Color DARK_GRAY = Color.rgb(85, 85, 85);
    public static Color BLACK = Color.rgb(0, 0, 0);

    public int red, green, blue, alpha, asInt;
    public String asHex;

    private Color(int red, int green, int blue, int alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
        this.asInt = (this.alpha << 24) + (this.blue << 16) + (this.green << 8) + this.red;
        this.asHex = Integer.toHexString(asInt).substring(2);
    }

    public static Color rgb(int r, int g, int b) {
        return Color.rgba(r, g, b, 255);
    }

    public static Color rgba(int r, int g, int b, int a) {
        return new Color(r, g, b, a);
    }

    public static Color hex(String hex) {
        if (hex.length() != 6) return Color.BLACK;
        String sr = hex.substring(0,2);
        String sg = hex.substring(2,4);
        String sb = hex.substring(4,6);
        return Color.rgb(
                Integer.parseInt(sr, 16),
                Integer.parseInt(sg, 16),
                Integer.parseInt(sb, 16)
        );
    }
}
