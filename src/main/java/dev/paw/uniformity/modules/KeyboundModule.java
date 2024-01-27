package dev.paw.uniformity.modules;

import net.minecraft.client.option.KeyBinding;

public abstract class KeyboundModule extends Module {
    public KeyBinding keyBind;

    public KeyboundModule(String name, int defaultKey) {
        super(name);
        this.keyBind = new KeyBinding("dev.paw.uniformity.keybind."+name.toLowerCase(), defaultKey, "dev.paw.uniformity.name");
    }
}
