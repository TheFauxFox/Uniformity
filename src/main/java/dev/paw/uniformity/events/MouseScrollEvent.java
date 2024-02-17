package dev.paw.uniformity.events;

public class MouseScrollEvent extends CancellableEvent {
    public final double scrollDelta;

    public MouseScrollEvent(double delta) {
        scrollDelta = delta;
    }
}
