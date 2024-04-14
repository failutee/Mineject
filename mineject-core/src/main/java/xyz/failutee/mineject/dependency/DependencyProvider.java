package xyz.failutee.mineject.dependency;

import java.util.Set;

public interface DependencyProvider {

    <T> T getDependency(Class<T> tClass);

    <T> Set<T> getDependencies(Class<T> tClass);

}
