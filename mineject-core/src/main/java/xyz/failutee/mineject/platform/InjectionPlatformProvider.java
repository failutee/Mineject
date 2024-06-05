package xyz.failutee.mineject.platform;

import xyz.failutee.mineject.dependency.DependencyProvider;

@FunctionalInterface
public interface InjectionPlatformProvider {

    InjectionPlatform getPlatform(DependencyProvider dependencyProvider);

}
