package xyz.failutee.mineject;

import xyz.failutee.mineject.bean.BeanInvoker;
import xyz.failutee.mineject.bean.BeanManager;
import xyz.failutee.mineject.bean.BeanProcessor;
import xyz.failutee.mineject.bean.BeanSetupRegistry;
import xyz.failutee.mineject.dependency.DependencyComponents;
import xyz.failutee.mineject.dependency.DependencyResolver;
import xyz.failutee.mineject.event.EventDispatcher;
import xyz.failutee.mineject.event.EventDispatcherProvider;
import xyz.failutee.mineject.injector.DependencyInjector;
import xyz.failutee.mineject.dependency.DependencyProvider;
import xyz.failutee.mineject.platform.InjectionPlatform;
import xyz.failutee.mineject.platform.InjectionPlatformProvider;
import xyz.failutee.mineject.dependency.DependencyContext;
import xyz.failutee.mineject.settings.DependencySettings;
import xyz.failutee.mineject.subscribe.SubscriberRegistry;
import xyz.failutee.mineject.util.ClassScannerUtil;

import java.util.Set;

public class Mineject implements DependencyInjector, EventDispatcherProvider {

    private boolean isInitialized = false;

    private final DependencySettings dependencySettings;
    private final BeanManager beanManager;
    private final BeanProcessor beanProcessor;
    private final BeanInvoker beanInvoker;
    private final BeanSetupRegistry beanSetupRegistry;
    private final DependencyProvider dependencyProvider;
    private final InjectionPlatformProvider platformProvider;
    private final SubscriberRegistry subscriberRegistry;
    private final EventDispatcher eventDispatcher;
    private final DependencyResolver dependencyResolver;
    private final DependencyContext dependencyContext;

    protected Mineject(
        DependencySettings dependencySettings,
        BeanManager beanManager,
        BeanProcessor beanProcessor,
        BeanInvoker beanInvoker,
        BeanSetupRegistry beanSetupRegistry,
        DependencyProvider dependencyProvider,
        InjectionPlatformProvider platformProvider,
        SubscriberRegistry subscriberRegistry,
        EventDispatcher eventDispatcher,
        DependencyResolver dependencyResolver,
        DependencyContext dependencyContext
    ) {
        this.dependencySettings = dependencySettings;
        this.beanManager = beanManager;
        this.beanProcessor = beanProcessor;
        this.beanInvoker = beanInvoker;
        this.beanSetupRegistry = beanSetupRegistry;
        this.dependencyProvider = dependencyProvider;
        this.platformProvider = platformProvider;
        this.subscriberRegistry = subscriberRegistry;
        this.eventDispatcher = eventDispatcher;
        this.dependencyResolver = dependencyResolver;
        this.dependencyContext = dependencyContext;
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

        InjectionPlatform platform = this.platformProvider.getPlatform(this.dependencyContext);

        var dependencyComponents = new DependencyComponents(classes,
            this.dependencyResolver,
            this.beanInvoker,
            this.beanManager,
            this.beanProcessor,
            this.beanSetupRegistry
        );

        dependencyComponents.processBeans();
        dependencyComponents.createComponents();

        if (!InjectionPlatform.isEmpty(platform)) {
            platform.getProcessorConfigurer().configureProcessor(this.beanProcessor);
            dependencyComponents.processPlatform();
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
