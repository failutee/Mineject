package xyz.failutee.mineject.settings;

import xyz.failutee.mineject.dependency.DependencyContext;

@FunctionalInterface
public interface DependencySettingsConfigurer {

    void apply(DependencySettingsBuilder settings, DependencyContext dependencyContext);

}
