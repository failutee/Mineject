package xyz.failutee.mineject.bean;

import xyz.failutee.mineject.lifecycle.Cleanupable;

public abstract class AbstractBean<T> implements Bean<T>, Cleanupable {

    private final Class<? extends T> beanClass;

    private T instance;

    public AbstractBean(Class<? extends T> beanClass) {
        this.beanClass = beanClass;
    }

    @Override
    public boolean isInitialized() {
        return this.instance != null;
    }

    @Override
    public void initializeBean(T instance) {
        this.instance = instance;
    }

    @Override
    public T getInstance() {
        return this.instance;
    }

    @Override
    public Class<? extends T> getBeanClass() {
        return this.beanClass;
    }

    @Override
    public void cleanup() {
        this.instance = null;
    }
}
