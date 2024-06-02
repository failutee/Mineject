package xyz.failutee.mineject.dependency;

import xyz.failutee.mineject.annotation.Component;
import xyz.failutee.mineject.annotation.Injectable;
import xyz.failutee.mineject.bean.BeanManager;
import xyz.failutee.mineject.bean.BeanSetupRegistry;
import xyz.failutee.mineject.exception.DependencyException;
import xyz.failutee.mineject.platform.InjectionPlatform;
import xyz.failutee.mineject.processor.ClassProcessorManager;
import xyz.failutee.mineject.util.AnnotationUtil;
import xyz.failutee.mineject.util.ReflectionUtil;

import java.lang.reflect.*;
import java.util.Collection;

public class DependencyResolver {

    private final BeanManager beanManager;
    private final BeanSetupRegistry beanSetupRegistry;
    private final DependencyProvider dependencyProvider;
    private final Collection<Class<?>> classes;
    private final InjectionPlatform platform;

    public DependencyResolver(
        BeanManager beanManager,
        BeanSetupRegistry beanSetupRegistry,
        DependencyProvider dependencyProvider,
        Collection<Class<?>> classes,
        InjectionPlatform platform
    ) {
        this.beanManager = beanManager;
        this.beanSetupRegistry = beanSetupRegistry;
        this.dependencyProvider = dependencyProvider;
        this.classes = classes;
        this.platform = platform;
    }

    public void processBeans() {
        for (Method method : this.beanSetupRegistry.getBeanMethods()) {

            Object instance = this.invokeBeanMethod(method);

            this.beanManager.registerBean(instance.getClass(), instance);

        }
    }

    public void processPlatform() {
        ClassProcessorManager classProcessorManager = this.platform.getClassProcessorManager();

        for (Class<?> clazz : this.classes) {

            classProcessorManager.getClassProcessor(clazz).ifPresent(processor -> {
                Object instance = this.getOrCreateBean(clazz);

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

            Object instance = this.createInstance(clazz);

            this.beanManager.registerBean(clazz, instance);

        }
    }

    @SuppressWarnings("unchecked")
    private <T> T invokeBeanMethod(Method method) {
        Class<?> declaringClass = method.getDeclaringClass();
        Object[] args = this.resolveArguments(method);

        Object instance = this.getOrCreateBean(declaringClass);

        method.setAccessible(true);

        try {
            return (T) method.invoke(instance, args);
        } catch (Exception exception) {
            throw new DependencyException("Error invoking bean method for class '%s'.".formatted(declaringClass.getSimpleName()));
        }
    }

    public <T> T createInstance(Class<T> clazz) {
        Constructor<T> constructor = this.findConstructor(clazz, clazz.getDeclaredConstructors());

        constructor.setAccessible(true);

        Object[] args = this.resolveArguments(constructor);

        try {
            return ReflectionUtil.createNewInstance(constructor, args);
        } catch (Exception exception) {
            throw new DependencyException("There was a problem while creating an instance of the '%s' class.".formatted(clazz.getSimpleName()));
        }
    }

    private Object[] resolveArguments(Executable executable) {
        Parameter[] parameters = executable.getParameters();
        Object[] args = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {

            Class<?> type = parameters[i].getType();

            args[i] = this.getOrCreateBean(type);
        }

        return args;
    }

    @SuppressWarnings("unchecked")
    private <T> Constructor<T> findConstructor(Class<T> clazz, Constructor<?>[] constructors) {
        for (Constructor<?> constructor : constructors) {

            if (!constructor.isAnnotationPresent(Injectable.class)) {
                continue;
            }

            return (Constructor<T>) constructor;
        }

        try {
            return clazz.getDeclaredConstructor();
        } catch (Exception exception) {
            throw new DependencyException("Could not find constructor for '%s' class, did you forgot @Injectable?".formatted(clazz.getSimpleName()));
        }
    }

    private <T> Object getOrCreateBean(Class<? extends T> clazz) {
        return beanSetupRegistry.searchBeanInstance(clazz).orElseGet(() -> {
            if (this.beanManager.containsBean(clazz)) {
                return this.dependencyProvider.getDependency(clazz);
            }

            Object instance = this.createInstance(clazz);
            this.beanSetupRegistry.registerBeanType(instance);

            return instance;
        });
    }
}
