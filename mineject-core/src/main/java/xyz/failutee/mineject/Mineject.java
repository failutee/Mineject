package xyz.failutee.mineject;

import xyz.failutee.mineject.bean.*;
import xyz.failutee.mineject.dependency.DependencyContext;
import xyz.failutee.mineject.dependency.DependencyResolver;
import xyz.failutee.mineject.event.EventDispatcher;
import xyz.failutee.mineject.event.EventDispatcherProvider;
import xyz.failutee.mineject.event.impl.MinejectPreInitializeEvent;
import xyz.failutee.mineject.injector.DependencyInjector;
import xyz.failutee.mineject.dependency.DependencyProvider;
import xyz.failutee.mineject.platform.InjectionPlatform;
import xyz.failutee.mineject.platform.InjectionPlatformProvider;
import xyz.failutee.mineject.settings.DependencySettings;
import xyz.failutee.mineject.subscribe.SubscriberRegistry;
import xyz.failutee.mineject.util.ClassScannerUtil;

import java.util.List;
import java.util.Set;

public class Mineject implements DependencyInjector, EventDispatcherProvider {

    private boolean isInitialized = false;

    private final DependencySettings dependencySettings;
    private final SubscriberRegistry subscriberRegistry;
    private final DependencyResolver dependencyResolver;
    private final DependencyProvider dependencyProvider;
    private final InjectionPlatformProvider platformProvider;
    private final DependencyContext dependencyContext;
    private final EventDispatcher eventDispatcher;
    private final BeanProcessor beanProcessor;
    private final BeanService beanService;

    protected Mineject(
        DependencySettings dependencySettings,
        SubscriberRegistry subscriberRegistry,
        DependencyResolver dependencyResolver,
        DependencyProvider dependencyProvider,
        InjectionPlatformProvider platformProvider,
        DependencyContext dependencyContext,
        EventDispatcher eventDispatcher,
        BeanProcessor beanProcessor,
        BeanService beanService
    ) {
        this.dependencySettings = dependencySettings;
        this.subscriberRegistry = subscriberRegistry;
        this.dependencyResolver = dependencyResolver;
        this.dependencyProvider = dependencyProvider;
        this.platformProvider = platformProvider;
        this.dependencyContext = dependencyContext;
        this.eventDispatcher = eventDispatcher;
        this.beanProcessor = beanProcessor;
        this.beanService = beanService;
    }

    @Override
    public void runDependencyInjector() {
        if (this.isInitialized) {
            throw new RuntimeException("This function can only be executed once per instance.");
        }
        else this.isInitialized = true;

        String packageName = this.dependencySettings.getPackageName();
        ClassLoader classLoader = this.getClass().getClassLoader();

        List<Class<?>> classes = ClassScannerUtil.scanClasses(packageName, classLoader);

        InjectionPlatform platform = this.platformProvider.getPlatform(this.dependencyContext);

        if (!InjectionPlatform.isEmpty(platform)) {
            platform.getProcessorConfigurer().configureProcessor(this.beanProcessor);
        }

        this.subscriberRegistry.collectSubscribedMethods(classes);
        this.beanService.collectBeans(classes);

        this.eventDispatcher.dispatchEvent(new MinejectPreInitializeEvent());

        this.beanService.getBeans().forEach(beanHolder -> {
            Class<?> beanClass = beanHolder.beanClass();
            Bean<?> bean = beanHolder.bean();

            if (bean.isInitialized()) {
                return;
            }

            this.dependencyResolver.getOrInitialize(beanClass);
        });
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
