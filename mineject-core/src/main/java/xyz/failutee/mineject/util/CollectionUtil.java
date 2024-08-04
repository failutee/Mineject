package xyz.failutee.mineject.util;

import java.util.Collection;

public final class CollectionUtil {

    private CollectionUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be initialized");
    }

    public static boolean isAnyClassAssignableFrom(Collection<Class<?>> classSet, Class<?> clazz) {
        return classSet.stream().anyMatch(keyClass -> keyClass.isAssignableFrom(clazz));
    }
}
