package dev.paw.uniformity.utils;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.text.Text;

public class Str {
    public static String title(String s) {
        StringBuilder builder = new StringBuilder();
        for (String seg: s.split(" ")) {
            char firstChar = seg.toUpperCase().charAt(0);
            String rest = seg.toLowerCase();
            builder.append(firstChar).append(rest.substring(1)).append(" ");
        }
        return builder.substring(0, builder.length() - 1);
    }

    public static String titleIdentifier(String s) {
        return title(s.replace("minecraft:", "").replace("_", " "));
    }

    public static Text getEnchantName(Enchantment enchantment) {
        return enchantment.description().copy().append(enchantment.getMaxLevel() > 1 ? Text.translatable("enchantment.level." + enchantment.getMaxLevel()) : Text.empty());
    }
}
