package xyz.failutee.mineject;

import xyz.failutee.mineject.bean.BeanManager;
import xyz.failutee.mineject.bean.BeanSetupRegistry;
import xyz.failutee.mineject.dependency.DependencyProvider;
import xyz.failutee.mineject.dependency.DependencyProviderImpl;
import xyz.failutee.mineject.settings.DependencySettings;
import xyz.failutee.mineject.settings.DependencySettingsBuilder;
import xyz.failutee.mineject.settings.DependencySettingsConfigurer;

public class MinejectFactory {

    private final DependencySettingsBuilder settings;
    private final BeanManager beanManager;
    private final BeanSetupRegistry beanSetupRegistry;
    private final DependencyProvider dependencyProvider;

    private DependencySettingsConfigurer dependencySettingsConfigurer;

    public MinejectFactory() {
        this.settings = DependencySettings.DEFAULT_SETTINGS;
        this.beanManager = new BeanManager();
        this.beanSetupRegistry = new BeanSetupRegistry();
        this.dependencyProvider = new DependencyProviderImpl(this.beanManager);
    }

    public MinejectFactory dependencySettings(DependencySettingsConfigurer dependencySettingsConfigurer) {
        this.dependencySettingsConfigurer = dependencySettingsConfigurer;
        return this;
    }

    public <T> MinejectFactory withBean(Class<T> beanClass, T instance) {
        this.beanManager.registerBean(beanClass, instance);
        return this;
    }

    public Mineject build(boolean runInjection) {
        this.dependencySettingsConfigurer.apply(this.settings);

        var mineject = new Mineject(
            this.settings,
            this.beanManager,
            this.beanSetupRegistry,
            this.dependencyProvider
        );

        if (runInjection) {
            mineject.runDependencyInjector();
        }

        return mineject;
    }

    public Mineject build() {
        return this.build(false);
    }
}
