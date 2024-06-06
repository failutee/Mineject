package xyz.failutee.mineject.event;

public abstract class CancellableEvent implements Event {

    private boolean cancelled;

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
