package xyz.failutee.mineject.subscribe;

import xyz.failutee.mineject.event.Event;

import java.lang.reflect.Method;
import java.util.*;

public class SubscriberRegistry {

    private final Map<Class<? extends Event>, List<Method>> eventsMethods = new HashMap<>();

    public void registerEventClass(Class<? extends Event> eventClass, Method method) {
        this.eventsMethods.computeIfAbsent(eventClass, ignore -> new ArrayList<>()).add(method);
    }

    public void collectSubscribedMethods(Collection<Class<?>> classes) {

        for (Class<?> clazz : classes) {

            if (!Subscriber.class.isAssignableFrom(clazz)) {
                continue;
            }

            for (Method method : clazz.getDeclaredMethods()) {

                if (!method.isAnnotationPresent(Subscribe.class)) {
                    continue;
                }

                Subscribe subscribe = method.getAnnotation(Subscribe.class);

                this.registerEventClass(subscribe.value(), method);
            }
        }
    }

    public List<Method> getMethodsByEvent(Event event) {
        return this.eventsMethods.getOrDefault(event.getClass(), new ArrayList<>());
    }
}
