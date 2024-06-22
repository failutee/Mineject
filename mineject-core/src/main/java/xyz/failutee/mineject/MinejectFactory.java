package xyz.failutee.mineject;

import xyz.failutee.mineject.bean.BeanInvoker;
import xyz.failutee.mineject.bean.BeanManager;
import xyz.failutee.mineject.bean.BeanProcessor;
import xyz.failutee.mineject.bean.BeanSetupRegistry;
import xyz.failutee.mineject.dependency.DependencyProvider;
import xyz.failutee.mineject.dependency.DependencyProviderImpl;
import xyz.failutee.mineject.dependency.DependencyResolver;
import xyz.failutee.mineject.dependency.DependencyResolverImpl;
import xyz.failutee.mineject.event.EventDispatcher;
import xyz.failutee.mineject.event.EventDispatcherImpl;
import xyz.failutee.mineject.platform.InjectionPlatform;
import xyz.failutee.mineject.platform.InjectionPlatformProvider;
import xyz.failutee.mineject.settings.DependencySettings;
import xyz.failutee.mineject.settings.DependencySettingsBuilder;
import xyz.failutee.mineject.settings.DependencySettingsConfigurer;
import xyz.failutee.mineject.subscribe.SubscriberRegistry;

public class MinejectFactory {

    private final DependencySettingsBuilder settings;
    private final BeanSetupRegistry beanSetupRegistry;
    private final SubscriberRegistry subscriberRegistry;
    private final BeanManager beanManager;
    private final BeanProcessor beanProcessor;
    private final DependencyProvider dependencyProvider;
    private final DependencyResolver dependencyResolver;
    private final EventDispatcher eventDispatcher;
    private final BeanInvoker beanInvoker;

    private InjectionPlatformProvider platformProvider;
    private DependencySettingsConfigurer dependencySettingsConfigurer;

    public MinejectFactory() {
        this.settings = DependencySettings.DEFAULT_SETTINGS;
        this.platformProvider = (ignored) -> InjectionPlatform.EMPTY_PLATFORM;
        this.beanSetupRegistry = new BeanSetupRegistry();
        this.subscriberRegistry = new SubscriberRegistry();
        this.beanManager = new BeanManager();
        this.beanProcessor = new BeanProcessor();
        this.dependencyProvider = new DependencyProviderImpl(this.beanManager);
        this.dependencyResolver = new DependencyResolverImpl(this.beanManager, this.beanSetupRegistry, this.dependencyProvider);
        this.eventDispatcher = new EventDispatcherImpl(this.subscriberRegistry, this.dependencyResolver, this.dependencyProvider);
        this.beanInvoker = new BeanInvoker(this.dependencyResolver);
    }

    public MinejectFactory dependencySettings(DependencySettingsConfigurer dependencySettingsConfigurer) {
        this.dependencySettingsConfigurer = dependencySettingsConfigurer;
        return this;
    }

    public MinejectFactory platformProvider(InjectionPlatformProvider platformProvider) {
        this.platformProvider = platformProvider;
        return this;
    }

    public <T> MinejectFactory withBean(Class<T> beanClass, T instance) {
        this.beanManager.registerBean(beanClass, instance);
        return this;
    }

    public Mineject build(boolean runInjection) {
        if (this.dependencySettingsConfigurer != null) {
            this.dependencySettingsConfigurer.apply(this.settings);
        }

        this.settings.getProcessorConfigurer().configureProcessor(this.beanProcessor);

        var mineject = new Mineject(
            this.settings,
            this.beanManager,
            this.beanProcessor,
            this.beanInvoker,
            this.beanSetupRegistry,
            this.dependencyProvider,
            this.platformProvider,
            this.subscriberRegistry,
            this.eventDispatcher,
            this.dependencyResolver
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
