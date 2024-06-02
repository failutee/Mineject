package xyz.failutee.mineject.event;

@FunctionalInterface
public interface EventDispatcher {

    void dispatchEvent(Event event);

}
