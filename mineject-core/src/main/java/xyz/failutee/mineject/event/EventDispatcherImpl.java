package xyz.failutee.mineject.event;

import xyz.failutee.mineject.dependency.DependencyProvider;
import xyz.failutee.mineject.dependency.DependencyResolver;
import xyz.failutee.mineject.exception.DependencyException;
import xyz.failutee.mineject.subscribe.Subscribe;
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
            if (event instanceof CancellableEvent cancellableEvent && cancellableEvent.isCancelled()) {
                break;
            }
            this.invokeEventMethod(method, event);
        }

        return event;
    }

    private void invokeEventMethod(Method method, Event event) {
        Class<?> declaringClass = method.getDeclaringClass();

        Object instance = Modifier.isStatic(method.getModifiers()) ? null : this.getDependencyInstance(declaringClass);

        Object[] arguments = this.resolveEventMethodArguments(method, event);

        try {
            ReflectionUtil.invokeMethod(instance, method, arguments);
        }
        catch (Exception exception) {
            throw new DependencyException("There was a problem calling the event function '%s:%s' - '%s'".formatted(method.getDeclaringClass().getSimpleName(), method.getName(), event.getClass().getSimpleName()), exception);
        }
    }

    private Object[] resolveEventMethodArguments(Method method, Event event) {
        Subscribe subscribe = method.getAnnotation(Subscribe.class);

        Class<?>[] parameters = method.getParameterTypes();

        if (parameters.length > 0 && parameters[0] == subscribe.value()) {
            Object[] arguments = this.dependencyResolver.resolveArguments(method, 1);
            arguments[0] = event;

            return arguments;
        }

        return this.dependencyResolver.resolveArguments(method);
    }

    private <T> T getDependencyInstance(Class<? extends T> declaringClass) {
        try {
            return this.dependencyProvider.getDependency(declaringClass);
        } catch (DependencyException exception) {
            return this.dependencyProvider.getOrCreate(declaringClass);
        }
    }
}
