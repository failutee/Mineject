package xyz.failutee.mineject.dependency;

import java.lang.reflect.Executable;

public interface DependencyResolver {

    <T> T createInstance(Class<T> clazz);

    Object[] resolveArguments(Executable executable, int skip);

    Object[] resolveArguments(Executable executable);

    <T> T getOrInitialize(Class<? extends T> beanClass);

}
