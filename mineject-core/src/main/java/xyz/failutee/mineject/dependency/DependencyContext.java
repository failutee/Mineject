package xyz.failutee.mineject.dependency;

import xyz.failutee.mineject.event.EventDispatcher;

public interface DependencyContext {

    DependencyProvider dependencyProvider();

    EventDispatcher eventDispatcher();

    static DependencyContext create(DependencyProvider dependencyProvider, EventDispatcher eventDispatcher) {
        return new DependencyContextImpl(dependencyProvider, eventDispatcher);
    }

    record DependencyContextImpl(DependencyProvider dependencyProvider, EventDispatcher eventDispatcher) implements DependencyContext {
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
