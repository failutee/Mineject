package xyz.failutee.mineject;

import xyz.failutee.mineject.bean.BeanManager;
import xyz.failutee.mineject.bean.BeanSetupRegistry;
import xyz.failutee.mineject.dependency.DependencyResolver;
import xyz.failutee.mineject.injector.DependencyInjector;
import xyz.failutee.mineject.dependency.DependencyProvider;
import xyz.failutee.mineject.settings.DependencySettings;
import xyz.failutee.mineject.util.ClassScannerUtil;

import java.util.Set;

public class Mineject implements DependencyInjector {

    private boolean isInitialized = false;

    private final DependencySettings dependencySettings;
    private final BeanManager beanManager;
    private final BeanSetupRegistry beanSetupRegistry;
    private final DependencyProvider dependencyProvider;

    protected Mineject(
        DependencySettings dependencySettings,
        BeanManager beanManager,
        BeanSetupRegistry beanSetupRegistry,
        DependencyProvider dependencyProvider
    ) {
        this.dependencySettings = dependencySettings;
        this.beanManager = beanManager;
        this.beanSetupRegistry = beanSetupRegistry;
        this.dependencyProvider = dependencyProvider;
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

        var dependencyResolver = new DependencyResolver(this.beanManager, this.beanSetupRegistry, this.dependencyProvider, classes);

        dependencyResolver.processBeans();
        dependencyResolver.createComponents();
    }

    @Override
    public DependencyProvider getDependencyProvider() {
        return this.dependencyProvider;
    }
}
