package xyz.failutee.mineject.bean.impl;

import xyz.failutee.mineject.bean.AbstractBean;
import xyz.failutee.mineject.dependency.DependencyResolver;

public class ComponentBean<T> extends AbstractBean<T> {

    @Override
    public void handleBean(Class<? extends T> beanClass, DependencyResolver dependencyResolver) {
        T instance = dependencyResolver.createInstance(beanClass);
        this.initializeBean(instance);
    }
}
