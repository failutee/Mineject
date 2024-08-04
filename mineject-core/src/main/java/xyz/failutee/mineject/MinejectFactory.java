package xyz.failutee.mineject;

import xyz.failutee.mineject.bean.BeanProcessor;
import xyz.failutee.mineject.bean.BeanService;
import xyz.failutee.mineject.dependency.*;
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
    private final SubscriberRegistry subscriberRegistry;
    private final DependencyResolver dependencyResolver;
    private final BeanProcessor beanProcessor;
    private final BeanService beanService;
    private final DependencyProvider dependencyProvider;
    private final EventDispatcher eventDispatcher;
    private final DependencyContext dependencyContext;

    private InjectionPlatformProvider platformProvider;
    private DependencySettingsConfigurer dependencySettingsConfigurer;

    private MinejectFactory() {
        this.settings = DependencySettings.DEFAULT_SETTINGS;
        this.platformProvider = (ignored) -> InjectionPlatform.EMPTY_PLATFORM;
        this.subscriberRegistry = new SubscriberRegistry();
        this.beanProcessor = new BeanProcessor();
        this.beanService = new BeanService(this.beanProcessor);
        this.dependencyResolver = new DependencyResolverImpl(this.beanService);
        this.dependencyProvider = new DependencyProviderImpl(this.dependencyResolver, this.beanService);
        this.eventDispatcher = new EventDispatcherImpl(this.subscriberRegistry, this.dependencyResolver, this.dependencyProvider);
        this.dependencyContext = DependencyContext.create(this.dependencyProvider, this.eventDispatcher);
    }

    public static MinejectFactory create() {
        return new MinejectFactory();
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
        this.dependencyProvider.registerDependency(beanClass, instance);
        return this;
    }

    public Mineject build(boolean runInjection) {
        if (this.dependencySettingsConfigurer != null) {
            this.dependencySettingsConfigurer.apply(this.settings, this.dependencyContext);
        }

        this.settings.getProcessorConfigurer().configureProcessor(this.beanProcessor);

        var mineject = new Mineject(
            this.settings,
            this.subscriberRegistry,
            this.dependencyResolver,
            this.dependencyProvider,
            this.eventDispatcher,
            this.beanProcessor,
            this.beanService
        );

        this.registerDefaultBeans();

        if (runInjection) {
            mineject.runDependencyInjector();
        }

        return mineject;
    }

    private void registerDefaultBeans() {
        this.dependencyProvider.registerDependency(DependencyProvider.class, this.dependencyProvider);
        this.dependencyProvider.registerDependency(EventDispatcher.class, this.eventDispatcher);
    }

    public Mineject build() {
        return this.build(true);
    }
}
