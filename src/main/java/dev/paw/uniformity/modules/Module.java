package dev.paw.uniformity.modules;

import net.minecraft.client.MinecraftClient;

public abstract class Module {
    protected MinecraftClient mc = MinecraftClient.getInstance();
    public String name;

    public Module(String name) {
        this.name = name;
    }

    public abstract boolean isEnabled();
    public abstract void setEnabled(boolean value);

    public boolean toggle() {
        this.setEnabled(!this.isEnabled());
        return this.isEnabled();
    }

    public void onClientTick() {}
}
