package xyz.failutee.mineject.subscribe;

import xyz.failutee.mineject.event.Event;

import java.lang.reflect.Method;
import java.util.*;

public class SubscriberRegistry {

    private final Map<Class<? extends Event>, List<Method>> eventsMethods = new HashMap<>();

    public void registerEventClass(Class<? extends Event> eventClass, Method method) {
        this.eventsMethods.computeIfAbsent(eventClass, ignore -> new ArrayList<>()).add(method);
    }

    public void collectMethods(Collection<Class<?>> classes) {

        for (Class<?> clazz : classes) {

            if (!Subscriber.class.isAssignableFrom(clazz)) {
                continue;
            }

            for (Method method : clazz.getMethods()) {

                if (!method.isAnnotationPresent(Subscribe.class)) {
                    continue;
                }

                var parameterTypes = method.getParameterTypes();
                var parameter = parameterTypes[0];

                if (parameterTypes.length != 1) {
                    continue;
                }

                this.castToClassEvent(parameter).ifPresent(eventClass -> this.registerEventClass(eventClass, method));
            }

        }

    }

    @SuppressWarnings("unchecked")
    private Optional<Class<? extends Event>> castToClassEvent(Class<?> clazz) {
        if (!Event.class.isAssignableFrom(clazz)) {
            return Optional.empty();
        }

        return Optional.of((Class<? extends Event>) clazz);
    }

    public List<Method> getMethodsByEvent(Event event) {
        return this.eventsMethods.getOrDefault(event.getClass(), new ArrayList<>());
    }
}
