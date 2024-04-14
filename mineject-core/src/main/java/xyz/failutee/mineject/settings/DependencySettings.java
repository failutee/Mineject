package xyz.failutee.mineject.settings;

public interface DependencySettings {

    DependencySettingsBuilder DEFAULT_SETTINGS = new DependencySettingsBuilder()
            .packageName("*");

    String getPackageName();

}
