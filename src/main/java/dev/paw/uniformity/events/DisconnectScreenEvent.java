package dev.paw.uniformity.events;

import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;

public class DisconnectScreenEvent extends Event {
    public final Screen parent;
    public final DirectionalLayoutWidget grid;
    public final AddChildCallback cb;
    public DisconnectScreenEvent(Screen parent, DirectionalLayoutWidget grid, AddChildCallback screen) {
        this.parent = parent;
        this.grid = grid;
        cb = screen;
    }

    public interface AddChildCallback {
        <T extends Element & Drawable & Selectable> T addDrawableChild(T drawableElement);
    }

    public static class Tick extends Event {
        public final Screen parent;
        public Tick(Screen parent) {
            this.parent = parent;
        }
    }
}
