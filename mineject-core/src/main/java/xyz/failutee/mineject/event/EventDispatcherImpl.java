package xyz.failutee.mineject.event;

import xyz.failutee.mineject.dependency.DependencyProvider;
import xyz.failutee.mineject.dependency.DependencyResolver;
import xyz.failutee.mineject.exception.DependencyException;
import xyz.failutee.mineject.subscribe.SubscriberRegistry;
import xyz.failutee.mineject.util.ReflectionUtil;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class EventDispatcherImpl implements EventDispatcher {

    private final SubscriberRegistry registry;
    private final DependencyResolver dependencyResolver;
    private final DependencyProvider dependencyProvider;

    public EventDispatcherImpl(SubscriberRegistry registry, DependencyResolver dependencyResolver, DependencyProvider dependencyProvider) {
        this.registry = registry;
        this.dependencyResolver = dependencyResolver;
        this.dependencyProvider = dependencyProvider;
    }

    @Override
    public <T extends Event> T dispatchEvent(T event) {
        List<Method> methods = this.registry.getMethodsByEvent(event);

        for (Method method : methods) {
            if (!this.isValidEventMethod(method, event)) {
                continue;
            }

            this.invokeEventMethod(method, event);
        }

        return event;
    }

    private boolean isValidEventMethod(Method method, Event event) {
        return method.getParameterTypes()[0] == event.getClass();
    }

    private void invokeEventMethod(Method method, Event event) {
        Class<?> declaringClass = method.getDeclaringClass();
        Object instance = null;

        if (!Modifier.isStatic(method.getModifiers())) {
            instance = this.getDependencyInstance(declaringClass);
        }

        Object[] arguments = this.dependencyResolver.resolveArguments(method, 1);
        arguments[0] = event;

        try {
            ReflectionUtil.invokeMethod(instance, method, arguments);
        } catch (Exception exception) {
            throw new DependencyException("There was a problem calling the event function '%s:%s' - '%s'".formatted(method.getDeclaringClass().getSimpleName(), method.getName(), event.getClass().getSimpleName()));
        }
    }

    private <T> T getDependencyInstance(Class<? extends T> declaringClass) {
        try {
            return this.dependencyProvider.getDependency(declaringClass);
        } catch (DependencyException exception) {
            return this.dependencyResolver.createInstance(declaringClass);
        }
    }
}
