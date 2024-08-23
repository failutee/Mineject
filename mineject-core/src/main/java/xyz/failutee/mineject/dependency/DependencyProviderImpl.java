package xyz.failutee.mineject.dependency;

import xyz.failutee.mineject.bean.Bean;
import xyz.failutee.mineject.bean.BeanService;
import xyz.failutee.mineject.bean.impl.ComponentBean;
import xyz.failutee.mineject.exception.DependencyException;
import xyz.failutee.mineject.util.ReflectionUtil;

import java.util.Optional;
import java.util.Set;

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
        Optional<Bean<T>> optionalBean = this.beanService.getBean(beanClass);

        if (optionalBean.isEmpty()) {
            throw new DependencyException("Dependency not found for '%s' class".formatted(beanClass.getSimpleName()));
        }

        Bean<T> bean = optionalBean.get();

        if (!bean.isInitialized()) {
            throw new DependencyException("Bean '%s' is not initialized".formatted(beanClass.getSimpleName()));
        }

        return Set.of(bean.getInstance());
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
        this.beanService.getBean(tClass).ifPresentOrElse(bean -> bean.initializeBean(ReflectionUtil.unsafeCast(instance)), () -> {
            var bean = new ComponentBean<>(ReflectionUtil.unsafeCast(tClass));
            bean.initializeBean(instance);

            this.beanService.registerBean(tClass, bean);
        });
    }
}
