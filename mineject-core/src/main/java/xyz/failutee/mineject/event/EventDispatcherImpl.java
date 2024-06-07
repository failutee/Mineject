package xyz.failutee.mineject.event;

import xyz.failutee.mineject.dependency.DependencyProvider;
import xyz.failutee.mineject.exception.DependencyException;
import xyz.failutee.mineject.subscribe.SubscriberRegistry;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

public class EventDispatcherImpl implements EventDispatcher {

    private final SubscriberRegistry registry;
    private final DependencyProvider dependencyProvider;

    public EventDispatcherImpl(SubscriberRegistry registry, DependencyProvider dependencyProvider) {
        this.registry = registry;
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
        return method.getParameterCount() == 1 && method.getParameterTypes()[0] == event.getClass();
    }

    private void invokeEventMethod(Method method, Event event) {
        Class<?> declaringClass = method.getDeclaringClass();
        Object instance = null;

        if (!Modifier.isStatic(method.getModifiers())) {
            instance = this.getDependencyInstance(declaringClass);
        }

        this.invokeMethod(instance, method, event);
    }

    private <T> T getDependencyInstance(Class<? extends T> declaringClass) {
        try {
            return this.dependencyProvider.getDependency(declaringClass);
        } catch (DependencyException exception) {
            throw new DependencyException("No dependency found for '%s', did you forget @Component?".formatted(declaringClass.getSimpleName()));
        }
    }

    private void invokeMethod(Object instance, Method method, Event event) {
        try {
            method.setAccessible(true);
            method.invoke(instance, event);
        } catch (Exception exception) {
            throw new DependencyException("There was a problem calling the event function '%s:%s' - '%s'".formatted(method.getDeclaringClass().getSimpleName(), method.getName(), event.getClass().getSimpleName()));
        }
    }
}
