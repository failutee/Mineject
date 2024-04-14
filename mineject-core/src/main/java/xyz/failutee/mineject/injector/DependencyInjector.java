package xyz.failutee.mineject.injector;

import xyz.failutee.mineject.dependency.DependencyProvider;

public interface DependencyInjector {

    void runDependencyInjector();

    DependencyProvider getDependencyProvider();

}
