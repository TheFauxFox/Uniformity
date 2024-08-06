package dev.paw.uniformity.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.registry.entry.RegistryEntry;

public class Plr {
    private static final MinecraftClient mc = MinecraftClient.getInstance();

    public static void setPlayerAttribute(RegistryEntry<EntityAttribute> attribute, double value) {
        if (mc.player == null) return;
        EntityAttributeInstance attribs = mc.player.getAttributeInstance(attribute);
        if (attribs == null) return;
        attribs.setBaseValue(value);
    }
}
