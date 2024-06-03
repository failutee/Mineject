package xyz.failutee.mineject.settings;

@FunctionalInterface
public interface DependencySettingsConfigurer {

    void apply(DependencySettingsBuilder settings);

}
