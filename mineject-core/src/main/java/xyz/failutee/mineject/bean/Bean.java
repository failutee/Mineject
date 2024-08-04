package xyz.failutee.mineject.bean;

import xyz.failutee.mineject.dependency.DependencyResolver;

public interface Bean<T> {

    void initializeBean(T instance);

    boolean isInitialized();

    T getInstance();

    void handleBean(Class<? extends T> beanClass, DependencyResolver dependencyResolver);

}
