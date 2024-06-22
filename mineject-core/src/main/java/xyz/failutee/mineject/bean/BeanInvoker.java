package xyz.failutee.mineject.bean;

import xyz.failutee.mineject.dependency.DependencyResolver;
import xyz.failutee.mineject.exception.DependencyException;

import java.lang.reflect.Method;

public class BeanInvoker {

    private final DependencyResolver dependencyResolver;

    public BeanInvoker(DependencyResolver dependencyResolver) {
        this.dependencyResolver = dependencyResolver;
    }

    @SuppressWarnings("unchecked")
    public <T> T invokeBeanMethod(Method method) {
        Class<?> declaringClass = method.getDeclaringClass();

        Object[] args = this.dependencyResolver.resolveArguments(method);
        Object instance = this.dependencyResolver.getOrCreateBean(declaringClass);

        method.setAccessible(true);

        try {
            return (T) method.invoke(instance, args);
        } catch (Exception exception) {
            throw new DependencyException("Error invoking bean method for class '%s'.".formatted(declaringClass.getSimpleName()));
        }
    }
}
