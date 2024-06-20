package xyz.failutee.mineject.dependency;

import xyz.failutee.mineject.annotation.Component;
import xyz.failutee.mineject.bean.BeanInvoker;
import xyz.failutee.mineject.bean.BeanManager;
import xyz.failutee.mineject.bean.BeanSetupRegistry;
import xyz.failutee.mineject.platform.InjectionPlatform;
import xyz.failutee.mineject.processor.ClassProcessorManager;
import xyz.failutee.mineject.util.AnnotationUtil;

import java.lang.reflect.Method;
import java.util.Set;

public class DependencyComponents {

    private final DependencyResolver dependencyResolver;
    private final BeanInvoker beanInvoker;
    private final BeanManager beanManager;
    private final BeanSetupRegistry beanSetupRegistry;
    private final InjectionPlatform platform;
    private final Set<Class<?>> classes;

    public DependencyComponents(
        DependencyResolver dependencyResolver,
        BeanInvoker beanInvoker,
        BeanManager beanManager,
        BeanSetupRegistry beanSetupRegistry,
        InjectionPlatform platform,
        Set<Class<?>> classes
    ) {
        this.dependencyResolver = dependencyResolver;
        this.beanInvoker = beanInvoker;
        this.beanManager = beanManager;
        this.beanSetupRegistry = beanSetupRegistry;
        this.platform = platform;
        this.classes = classes;
    }

    public void processBeans() {
        for (Method method : this.beanSetupRegistry.getBeanMethods()) {

            Object instance = this.beanInvoker.invokeBeanMethod(method);

            this.beanManager.registerBean(method.getReturnType(), instance);

        }
    }

    public void processPlatform() {
        ClassProcessorManager classProcessorManager = this.platform.getClassProcessorManager();

        for (Class<?> clazz : this.classes) {

            classProcessorManager.getClassProcessor(clazz).ifPresent(processor -> {
                Object instance = this.dependencyResolver.getOrCreateBean(clazz);

                processor.processClass(instance);
            });

        }
    }

    public void createComponents() {
        for (Class<?> clazz : this.classes) {

            if (!AnnotationUtil.hasAnnotation(clazz, Component.class)) {
                continue;
            }

            if (this.beanManager.containsBean(clazz)) {
                continue;
            }

            Object instance = this.dependencyResolver.createInstance(clazz);

            this.beanManager.registerBean(clazz, instance);

        }
    }
}
