package xyz.failutee.mineject.subscribe;

import xyz.failutee.mineject.event.Event;
import xyz.failutee.mineject.util.ReflectionUtil;

import java.lang.reflect.Method;
import java.util.*;

public class SubscriberRegistry {

    private final Map<Class<? extends Event>, List<Method>> eventsMethods = new HashMap<>();

    public void registerEventClass(Class<? extends Event> eventClass, Method method) {
        this.eventsMethods.computeIfAbsent(eventClass, ignore -> new ArrayList<>()).add(method);
    }

    public void collectMethods(Collection<Class<?>> classes) {

        for (Class<?> clazz : classes) {

            System.out.println(clazz.getSimpleName());

            if (!Subscriber.class.isAssignableFrom(clazz)) {
                continue;
            }

            System.out.println("assignable");

            for (Method method : clazz.getDeclaredMethods()) {

                if (!method.isAnnotationPresent(Subscribe.class)) {
                    continue;
                }

                var parameterTypes = method.getParameterTypes();
                var parameter = parameterTypes[0];

                this.castToClassEvent(parameter).ifPresent(eventClass -> this.registerEventClass(eventClass, method));
            }

        }

    }

    private Optional<Class<? extends Event>> castToClassEvent(Class<?> clazz) {
        if (!Event.class.isAssignableFrom(clazz)) {
            return Optional.empty();
        }

        return Optional.of(ReflectionUtil.unsafeCast(clazz));
    }

    public List<Method> getMethodsByEvent(Event event) {
        return this.eventsMethods.getOrDefault(event.getClass(), new ArrayList<>());
    }
}
