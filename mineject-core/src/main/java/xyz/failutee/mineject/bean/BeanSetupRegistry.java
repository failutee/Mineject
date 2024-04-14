package xyz.failutee.mineject.bean;

import xyz.failutee.mineject.annotation.Bean;
import xyz.failutee.mineject.annotation.BeanSetup;
import xyz.failutee.mineject.util.AnnotationUtil;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

public class BeanSetupRegistry {

    private final Map<Type, Method> registeredBeanMethods = new HashMap<>();
    private final Set<Object> setupBeanInstances = new HashSet<>();

    public void registerBeanType(Object instance) {
        this.setupBeanInstances.add(instance);
    }

    public Optional<Object> searchBeanInstance(Class<?> clazz) {
        return this.setupBeanInstances.stream()
                .filter(instance -> instance.getClass() == clazz)
                .findFirst();
    }

    public Set<Method> getBeanMethods() {
        return new HashSet<>(this.registeredBeanMethods.values());
    }

    public void collectBeanMethods(Collection<Class<?>> classes) {
        for (Class<?> clazz : classes) {

            if (!AnnotationUtil.hasAnnotation(clazz, BeanSetup.class)) {
                continue;
            }

            for (Method method : clazz.getDeclaredMethods()) {

                if (!AnnotationUtil.hasAnnotation(method, Bean.class)) {
                    continue;
                }

                this.registeredBeanMethods.put(method.getReturnType(), method);

            }
        }
    }
}
