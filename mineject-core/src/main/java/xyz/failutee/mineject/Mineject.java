package xyz.failutee.mineject;

import xyz.failutee.mineject.bean.BeanManager;
import xyz.failutee.mineject.bean.BeanSetupRegistry;
import xyz.failutee.mineject.dependency.DependencyResolver;
import xyz.failutee.mineject.event.EventDispatcher;
import xyz.failutee.mineject.event.EventDispatcherProvider;
import xyz.failutee.mineject.injector.DependencyInjector;
import xyz.failutee.mineject.dependency.DependencyProvider;
import xyz.failutee.mineject.platform.InjectionPlatform;
import xyz.failutee.mineject.platform.InjectionPlatformProvider;
import xyz.failutee.mineject.platform.PlatformContext;
import xyz.failutee.mineject.settings.DependencySettings;
import xyz.failutee.mineject.subscribe.SubscriberRegistry;
import xyz.failutee.mineject.util.ClassScannerUtil;

import java.util.Set;

public class Mineject implements DependencyInjector, EventDispatcherProvider {

    private boolean isInitialized = false;

    private final DependencySettings dependencySettings;
    private final BeanManager beanManager;
    private final BeanSetupRegistry beanSetupRegistry;
    private final DependencyProvider dependencyProvider;
    private final InjectionPlatformProvider platformProvider;
    private final SubscriberRegistry subscriberRegistry;
    private final EventDispatcher eventDispatcher;

    protected Mineject(
        DependencySettings dependencySettings,
        BeanManager beanManager,
        BeanSetupRegistry beanSetupRegistry,
        DependencyProvider dependencyProvider,
        InjectionPlatformProvider platformProvider,
        SubscriberRegistry subscriberRegistry,
        EventDispatcher eventDispatcher
    ) {
        this.dependencySettings = dependencySettings;
        this.beanManager = beanManager;
        this.beanSetupRegistry = beanSetupRegistry;
        this.dependencyProvider = dependencyProvider;
        this.platformProvider = platformProvider;
        this.subscriberRegistry = subscriberRegistry;
        this.eventDispatcher = eventDispatcher;
    }

    @Override
    public void runDependencyInjector() {
        if (this.isInitialized) {
            throw new RuntimeException("This function can only be executed once per instance.");
        }
        else this.isInitialized = true;

        String packageName = this.dependencySettings.getPackageName();
        ClassLoader classLoader = this.getClass().getClassLoader();

        Set<Class<?>> classes = ClassScannerUtil.scanClasses(packageName, classLoader);

        this.beanSetupRegistry.collectBeanMethods(classes);
        this.subscriberRegistry.collectMethods(classes);

        PlatformContext platformContext = PlatformContext.create(this.dependencyProvider, this.eventDispatcher);
        InjectionPlatform platform = this.platformProvider.getPlatform(platformContext);

        var dependencyResolver = new DependencyResolver(this.beanManager, this.beanSetupRegistry, this.dependencyProvider, classes, platform);

        dependencyResolver.processBeans();
        dependencyResolver.createComponents();

        if (!InjectionPlatform.isEmpty(platform)) {
            dependencyResolver.processPlatform();
        }
    }

    @Override
    public DependencyProvider getDependencyProvider() {
        return this.dependencyProvider;
    }

    @Override
    public EventDispatcher getEventDispatcher() {
        return this.eventDispatcher;
    }
}
