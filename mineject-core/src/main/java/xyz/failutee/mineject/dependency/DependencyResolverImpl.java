package xyz.failutee.mineject.dependency;

import xyz.failutee.mineject.bean.BeanManager;
import xyz.failutee.mineject.bean.BeanSetupRegistry;
import xyz.failutee.mineject.exception.DependencyException;
import xyz.failutee.mineject.util.ReflectionUtil;

import java.lang.reflect.*;

public class DependencyResolverImpl implements DependencyResolver {

    private final BeanManager beanManager;
    private final BeanSetupRegistry beanSetupRegistry;
    private final DependencyProvider dependencyProvider;

    public DependencyResolverImpl(
        BeanManager beanManager,
        BeanSetupRegistry beanSetupRegistry,
        DependencyProvider dependencyProvider
    ) {
        this.beanManager = beanManager;
        this.beanSetupRegistry = beanSetupRegistry;
        this.dependencyProvider = dependencyProvider;
    }

    @Override
    public <T> T createInstance(Class<T> clazz) {
        Constructor<T> constructor = ReflectionUtil.findConstructor(clazz, clazz.getDeclaredConstructors());

        constructor.setAccessible(true);

        Object[] args = this.resolveArguments(constructor);

        try {
            return ReflectionUtil.createNewInstance(constructor, args);
        } catch (Exception exception) {
            throw new DependencyException("There was a problem while creating an instance of the '%s' class.".formatted(clazz.getSimpleName()));
        }
    }

    @Override
    public Object[] resolveArguments(Executable executable, int skip) {
        Parameter[] parameters = executable.getParameters();
        Object[] args = new Object[parameters.length];

        for (int i = skip; i < parameters.length; i++) {

            Class<?> type = parameters[i].getType();

            args[i] = this.getOrCreateBean(type);
        }

        return args;
    }

    @Override
    public Object[] resolveArguments(Executable executable) {
        return this.resolveArguments(executable, 0);
    }

    @Override
    public <T> Object getOrCreateBean(Class<? extends T> clazz) {
        return this.beanSetupRegistry.searchBeanInstance(clazz).orElseGet(() -> {
            if (this.beanManager.containsBean(clazz)) {
                return this.dependencyProvider.getDependency(clazz);
            }

            Object instance = this.createInstance(clazz);
            this.beanSetupRegistry.registerBeanType(instance);

            return instance;
        });
    }
}
