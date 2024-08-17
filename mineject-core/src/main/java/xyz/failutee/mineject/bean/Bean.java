package xyz.failutee.mineject.bean;

import xyz.failutee.mineject.dependency.DependencyResolver;

public interface Bean<T> {

    Class<? extends T> getBeanClass();

    void initializeBean(T instance);

    boolean isInitialized();

    T getInstance();

    void handleBean(DependencyResolver dependencyResolver);

}
