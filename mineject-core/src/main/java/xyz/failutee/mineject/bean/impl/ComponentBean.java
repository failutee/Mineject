package xyz.failutee.mineject.bean.impl;

import xyz.failutee.mineject.bean.AbstractBean;
import xyz.failutee.mineject.dependency.DependencyResolver;

public class ComponentBean<T> extends AbstractBean<T> {

    public ComponentBean(Class<? extends T> beanClass) {
        super(beanClass);
    }

    @Override
    public void handleBean(DependencyResolver dependencyResolver) {
        T instance = dependencyResolver.createInstance(this.getBeanClass());
        this.initializeBean(instance);
    }
}
