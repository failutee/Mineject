package xyz.failutee.mineject.dependency;

import java.lang.reflect.Executable;

public interface DependencyResolver {

    <T> Object getOrCreateBean(Class<? extends T> clazz);

    <T> T createInstance(Class<T> clazz);

    Object[] resolveArguments(Executable executable, int skip);

    Object[] resolveArguments(Executable executable);

}
