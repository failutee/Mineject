package xyz.failutee.mineject;

import xyz.failutee.mineject.annotation.Component;
import xyz.failutee.mineject.bean.BeanInvoker;
import xyz.failutee.mineject.bean.BeanManager;
import xyz.failutee.mineject.bean.BeanSetupRegistry;
import xyz.failutee.mineject.dependency.DependencyComponents;
import xyz.failutee.mineject.dependency.DependencyResolver;
import xyz.failutee.mineject.dependency.DependencyResolverImpl;
import xyz.failutee.mineject.event.EventDispatcher;
import xyz.failutee.mineject.event.EventDispatcherProvider;
import xyz.failutee.mineject.injector.DependencyInjector;
import xyz.failutee.mineject.dependency.DependencyProvider;
import xyz.failutee.mineject.platform.InjectionPlatform;
import xyz.failutee.mineject.platform.InjectionPlatformProvider;
import xyz.failutee.mineject.platform.PlatformContext;
import xyz.failutee.mineject.processor.ClassProcessorManager;
import xyz.failutee.mineject.settings.DependencySettings;
import xyz.failutee.mineject.subscribe.SubscriberRegistry;
import xyz.failutee.mineject.util.AnnotationUtil;
import xyz.failutee.mineject.util.ClassScannerUtil;

import java.lang.reflect.Method;
import java.util.Set;

public class Mineject implements DependencyInjector, EventDispatcherProvider {

    private boolean isInitialized = false;

    private final DependencySettings dependencySettings;
    private final BeanManager beanManager;
    private final BeanInvoker beanInvoker;
    private final BeanSetupRegistry beanSetupRegistry;
    private final DependencyProvider dependencyProvider;
    private final InjectionPlatformProvider platformProvider;
    private final SubscriberRegistry subscriberRegistry;
    private final EventDispatcher eventDispatcher;
    private final DependencyResolver dependencyResolver;

    protected Mineject(
        DependencySettings dependencySettings,
        BeanManager beanManager,
        BeanInvoker beanInvoker,
        BeanSetupRegistry beanSetupRegistry,
        DependencyProvider dependencyProvider,
        InjectionPlatformProvider platformProvider,
        SubscriberRegistry subscriberRegistry,
        EventDispatcher eventDispatcher,
        DependencyResolver dependencyResolver
    ) {
        this.dependencySettings = dependencySettings;
        this.beanManager = beanManager;
        this.beanInvoker = beanInvoker;
        this.beanSetupRegistry = beanSetupRegistry;
        this.dependencyProvider = dependencyProvider;
        this.platformProvider = platformProvider;
        this.subscriberRegistry = subscriberRegistry;
        this.eventDispatcher = eventDispatcher;
        this.dependencyResolver = dependencyResolver;
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

        var dependencyComponents = new DependencyComponents(
          this.dependencyResolver,
          this.beanInvoker,
          this.beanManager,
          this.beanSetupRegistry,
          platform, classes
        );

        dependencyComponents.processBeans();
        dependencyComponents.createComponents();

        if (!InjectionPlatform.isEmpty(platform)) {
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
