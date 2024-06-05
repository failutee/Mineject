package xyz.failutee.mineject.event;

@FunctionalInterface
public interface EventDispatcherProvider {

    EventDispatcher getEventDispatcher();

}
