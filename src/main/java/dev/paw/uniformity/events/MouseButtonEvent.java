package dev.paw.uniformity.events;

public class MouseButtonEvent extends CancellableEvent {
    public final Button button;

    public MouseButtonEvent(Button button) {
        this.button = button;
    }

    public enum Button {
        Left,
        Right,
        Middle
    }
}
