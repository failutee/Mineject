package xyz.failutee.mineject.util;

import java.lang.reflect.Constructor;

public final class ReflectionUtil {

    private ReflectionUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be initialized");
    }

    public static <T> T createNewInstance(Constructor<T> constructor, Object... args) throws Exception {
        if (args.length == 0) {
            return constructor.newInstance();
        }

        constructor = constructor.getDeclaringClass().getConstructor(constructor.getParameterTypes());

        constructor.setAccessible(true);

        return constructor.newInstance(args);
    }
}
