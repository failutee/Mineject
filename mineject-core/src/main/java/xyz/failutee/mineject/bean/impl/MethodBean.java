package xyz.failutee.mineject.bean.impl;

import xyz.failutee.mineject.bean.AbstractBean;
import xyz.failutee.mineject.dependency.DependencyResolver;
import xyz.failutee.mineject.exception.DependencyException;
import xyz.failutee.mineject.util.ReflectionUtil;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class MethodBean<T> extends AbstractBean<T> {

    private final Method beanMethod;

    public MethodBean(Method beanMethod) {
        this.beanMethod = beanMethod;
    }

    @Override
    public void handleBean(Class<? extends T> beanClass, DependencyResolver dependencyResolver) {
        Class<?> declaringClass = this.beanMethod.getDeclaringClass();

        Object[] args = dependencyResolver.resolveArguments(this.beanMethod);

        Object declaringInstance = Modifier.isStatic(this.beanMethod.getModifiers()) ? null : dependencyResolver.getOrInitialize(declaringClass);

        this.beanMethod.setAccessible(true);

        try {
            Object instance = this.beanMethod.invoke(declaringInstance, args);
            this.initializeBean(ReflectionUtil.unsafeCast(instance));
        } catch (Exception exception) {
            throw new DependencyException("Error invoking bean method for class '%s'.".formatted(declaringClass.getSimpleName()), exception);
        }
    }
}
