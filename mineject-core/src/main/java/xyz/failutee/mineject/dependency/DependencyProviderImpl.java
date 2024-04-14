package xyz.failutee.mineject.dependency;

import xyz.failutee.mineject.bean.Bean;
import xyz.failutee.mineject.bean.BeanManager;
import xyz.failutee.mineject.exception.DependencyException;

import java.util.Set;
import java.util.stream.Collectors;

public class DependencyProviderImpl implements DependencyProvider {

    private final BeanManager beanManager;

    public DependencyProviderImpl(BeanManager beanManager) {
        this.beanManager = beanManager;
    }

    @Override
    public <T> T getDependency(Class<T> tClass) {
        Set<T> dependencies = this.getDependencies(tClass);
        return dependencies.iterator().next();
    }

    @Override
    public <T> Set<T> getDependencies(Class<T> tClass) {
        Set<T> instances = this.beanManager.searchBeansByClass(tClass)
                .stream()
                .map(Bean::getInstance)
                .map(tClass::cast)
                .collect(Collectors.toSet());

        if (instances.isEmpty()) {
            throw new DependencyException("Dependency not found for '%s' class".formatted(tClass.getSimpleName()));
        }

        return instances;
    }
}
