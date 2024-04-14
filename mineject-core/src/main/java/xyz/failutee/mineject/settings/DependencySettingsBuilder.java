package xyz.failutee.mineject.settings;

public class DependencySettingsBuilder implements DependencySettings {

    private String packageName;

    public DependencySettingsBuilder packageName(String packageName) {
        this.packageName = packageName;
        return this;
    }

    @Override
    public String getPackageName() {
        return this.packageName;
    }
}
