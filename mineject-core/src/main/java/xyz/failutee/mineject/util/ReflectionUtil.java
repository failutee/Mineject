package xyz.failutee.mineject.util;

import xyz.failutee.mineject.annotation.Injectable;
import xyz.failutee.mineject.exception.DependencyException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class ReflectionUtil {

    private ReflectionUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be initialized");
    }

    @SuppressWarnings("unchecked")
    public static <T> T unsafeCast(Object object) {
        return (T) object;
    }

    public static <T> T createNewInstance(Constructor<T> constructor, Object... args) throws Exception {
        if (args.length == 0) {
            return constructor.newInstance();
        }

        constructor = constructor.getDeclaringClass().getConstructor(constructor.getParameterTypes());

        constructor.setAccessible(true);

        return constructor.newInstance(args);
    }

    public static <T> Constructor<T> findConstructor(Class<T> clazz, Constructor<?>[] constructors) {
        for (Constructor<?> constructor : constructors) {

            if (!constructor.isAnnotationPresent(Injectable.class)) {
                continue;
            }

            return ReflectionUtil.unsafeCast(constructor);
        }

        try {
            return clazz.getDeclaredConstructor();
        } catch (Exception exception) {
            throw new DependencyException("Could not find constructor for '%s' class, did you forgot @Injectable?".formatted(clazz.getSimpleName()), exception);
        }
    }

    public static void invokeMethod(Object instance, Method method, Object... args) throws InvocationTargetException, IllegalAccessException {
        method.setAccessible(true);
        method.invoke(instance, args);
    }
}
