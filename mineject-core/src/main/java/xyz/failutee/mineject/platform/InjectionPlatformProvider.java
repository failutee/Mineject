package xyz.failutee.mineject.platform;

import xyz.failutee.mineject.dependency.DependencyContext;

@FunctionalInterface
public interface InjectionPlatformProvider {

    InjectionPlatform getPlatform(DependencyContext context);

}
