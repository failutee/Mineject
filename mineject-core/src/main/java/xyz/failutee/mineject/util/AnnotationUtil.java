package xyz.failutee.mineject.util;

import xyz.failutee.mineject.annotation.Bean;
import xyz.failutee.mineject.annotation.BeanSetup;
import xyz.failutee.mineject.annotation.Component;

import java.lang.reflect.Method;

public final class AnnotationUtil {

    private AnnotationUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be initialized");
    }

    public static boolean isComponent(Class<?> clazz) {
        return clazz.isAnnotationPresent(Component.class);
    }

    public static boolean isBeanSetup(Class<?> clazz) {
        return clazz.isAnnotationPresent(BeanSetup.class);
    }

    public static boolean isMethodBean(Method method) {
        return method.isAnnotationPresent(Bean.class);
    }
}
