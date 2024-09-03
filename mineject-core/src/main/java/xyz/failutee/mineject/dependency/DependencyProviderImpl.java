package xyz.failutee.mineject.dependency;

import xyz.failutee.mineject.bean.Bean;
import xyz.failutee.mineject.bean.BeanService;
import xyz.failutee.mineject.bean.impl.ComponentBean;
import xyz.failutee.mineject.exception.DependencyException;
import xyz.failutee.mineject.util.ReflectionUtil;

import java.util.Set;
import java.util.stream.Collectors;

public class DependencyProviderImpl implements DependencyProvider {

    private final DependencyResolver dependencyResolver;
    private final BeanService beanService;

    public DependencyProviderImpl(DependencyResolver dependencyResolver, BeanService beanService) {
        this.dependencyResolver = dependencyResolver;
        this.beanService = beanService;
    }

    @Override
    public <T> T getDependency(Class<T> beanClass) {
        Set<T> dependencies = this.getDependencies(beanClass);

        return dependencies.iterator().next();
    }

    @Override
    public <T> Set<T> getDependencies(Class<T> beanClass) {
        Set<Bean<T>> beans = this.beanService.collectBeans(beanClass);

        if (beans.isEmpty()) {
            throw new DependencyException("Dependency not found for '%s' class.".formatted(beanClass.getSimpleName()));
        }

        for (Bean<T> bean : beans) {
            if (bean.isInitialized()) {
                continue;
            }
            throw new DependencyException("Bean '%s' has not been initialized but you're trying to get it.".formatted(bean.getBeanClass().getSimpleName()));
        }

        return beans.stream().map(Bean::getInstance).collect(Collectors.toSet());
    }

    @Override
    public <T> T getOrCreate(Class<T> beanClass) {
        try {
            return this.getDependency(beanClass);
        }
        catch (DependencyException exception) {
            T instance = this.dependencyResolver.getOrInitialize(beanClass);

            this.registerDependency(beanClass, instance);

            return instance;
        }
    }

    @Override
    public <T> void registerDependency(Class<? extends T> tClass, T instance) {
        var bean = new ComponentBean<>(ReflectionUtil.unsafeCast(tClass));
        bean.initializeBean(instance);

        this.beanService.registerBean(tClass, bean);
    }
}
