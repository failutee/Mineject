package xyz.failutee.mineject.dependency;

import xyz.failutee.mineject.annotation.Component;
import xyz.failutee.mineject.bean.BeanInvoker;
import xyz.failutee.mineject.bean.BeanManager;
import xyz.failutee.mineject.bean.BeanProcessor;
import xyz.failutee.mineject.bean.BeanSetupRegistry;
import xyz.failutee.mineject.util.AnnotationUtil;

import java.lang.reflect.Method;
import java.util.Set;

public class DependencyComponents {

    private final Set<Class<?>> classes;
    private final DependencyResolver dependencyResolver;
    private final BeanInvoker beanInvoker;
    private final BeanManager beanManager;
    private final BeanProcessor beanProcessor;
    private final BeanSetupRegistry beanSetupRegistry;

    public DependencyComponents(
        Set<Class<?>> classes,
        DependencyResolver dependencyResolver,
        BeanInvoker beanInvoker,
        BeanManager beanManager,
        BeanProcessor beanProcessor,
        BeanSetupRegistry beanSetupRegistry
    ) {
        this.classes = classes;
        this.dependencyResolver = dependencyResolver;
        this.beanInvoker = beanInvoker;
        this.beanManager = beanManager;
        this.beanProcessor = beanProcessor;
        this.beanSetupRegistry = beanSetupRegistry;
    }

    public void processBeans() {
        for (Method method : this.beanSetupRegistry.getBeanMethods()) {

            Object instance = this.beanInvoker.invokeBeanMethod(method);

            this.beanManager.registerBean(method.getReturnType(), instance);

        }
    }

    public void processPlatform() {
        for (Class<?> clazz : this.classes) {

            Object instance = this.dependencyResolver.getOrCreateBean(clazz);

            this.beanProcessor.processBean(clazz, instance);

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
