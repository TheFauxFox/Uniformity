package dev.paw.uniformity.events;

public abstract class CancellableEvent extends Event {
    private boolean cancelled = false;

    public boolean isCancelled() {
        return cancelled;
    }

    public void cancel() {
        setCancelled(true);
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
