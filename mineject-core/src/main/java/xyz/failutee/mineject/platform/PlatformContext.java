package xyz.failutee.mineject.platform;

import xyz.failutee.mineject.dependency.DependencyProvider;
import xyz.failutee.mineject.event.EventDispatcher;

public interface PlatformContext {

    DependencyProvider dependencyProvider();

    EventDispatcher eventDispatcher();

    static PlatformContext create(DependencyProvider dependencyProvider, EventDispatcher eventDispatcher) {
        return new PlatformContextImpl(dependencyProvider, eventDispatcher);
    }

    record PlatformContextImpl(DependencyProvider dependencyProvider, EventDispatcher eventDispatcher) implements PlatformContext {
        @Override
            public DependencyProvider dependencyProvider() {
                return this.dependencyProvider;
            }

            @Override
            public EventDispatcher eventDispatcher() {
                return this.eventDispatcher;
            }
        }

}
