package xyz.failutee.mineject.bean;

import java.util.*;
import java.util.stream.Collectors;

public class BeanManager {

    private final Map<Class<?>, Bean<?>> registeredBeans = new HashMap<>();

    public <T> void registerBean(Class<? extends T> beanClass, T instance) {
        Bean<T> bean = new Bean<T>(beanClass, () -> instance);

        this.registeredBeans.put(beanClass, bean);
    }

    public boolean containsBean(Class<?> beanClass) {
        return this.registeredBeans.containsKey(beanClass);
    }

    public Set<Bean<?>> searchBeansByClass(Class<?> clazz) {
        if (this.containsBean(clazz)) {
            Bean<?> bean = this.registeredBeans.get(clazz);
            return Set.of(bean);
        }

        return this.registeredBeans.values().stream()
                .filter(bean -> clazz.isAssignableFrom(bean.getInstanceClass()))
                .collect(Collectors.toSet());
    }
}