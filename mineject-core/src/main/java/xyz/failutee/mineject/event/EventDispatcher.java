package xyz.failutee.mineject.event;

@FunctionalInterface
public interface EventDispatcher {

    <T extends Event> T dispatchEvent(T event);

}
