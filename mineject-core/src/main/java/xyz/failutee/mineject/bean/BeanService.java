package xyz.failutee.mineject.bean;


import xyz.failutee.mineject.bean.impl.ComponentBean;
import xyz.failutee.mineject.bean.impl.MethodBean;
import xyz.failutee.mineject.bean.impl.ProcessedBean;
import xyz.failutee.mineject.util.AnnotationUtil;
import xyz.failutee.mineject.util.ReflectionUtil;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class BeanService {

    private final Map<Class<?>, Bean<?>> beans = new HashMap<>();

    private final BeanProcessor beanProcessor;

    public BeanService(BeanProcessor beanProcessor) {
        this.beanProcessor = beanProcessor;
    }

    public <T> void registerBean(Class<? extends T> beanClass, Bean<T> bean) {
        this.beans.put(beanClass, bean);
    }

    public <T> Optional<Bean<T>> getBean(Class<? extends T> beanClass) {
        if (this.beans.containsKey(beanClass)) {
            Bean<T> bean = ReflectionUtil.unsafeCast(this.beans.get(beanClass));
            return Optional.ofNullable(bean);
        }

        Set<Bean<T>> collectedBeans = this.collectBeans(beanClass);

        if (collectedBeans.isEmpty()) {
            return Optional.empty();
        }

        Bean<T> bean = collectedBeans.iterator().next();

        return Optional.of(bean);
    }

    public <T> Set<Bean<T>> collectBeans(Class<? extends T> beanClass) {
        return this.beans.entrySet().stream()
                .map(BeanHolder::fromEntry)
                .filter(holder -> beanClass.isAssignableFrom(holder.beanClass))
                .map(holder -> ReflectionUtil.<Bean<T>>unsafeCast(holder.bean))
                .collect(Collectors.toSet());
    }

    // TODO: Handle multiple bean types
    public void collectBeans(Collection<Class<?>> classes) {
        for (Class<?> clazz : classes) {

            if (this.beanProcessor.isProcessed(clazz)) {
                this.registerBean(clazz, new ProcessedBean<>(this.beanProcessor.getProcessors(clazz)));
            }

            if (AnnotationUtil.isComponent(clazz)) {
                this.registerBean(clazz, new ComponentBean<>());
            }

            if (AnnotationUtil.isBeanSetup(clazz)) {
                this.registerBean(clazz, new ComponentBean<>());

                for (Method method : clazz.getDeclaredMethods()) {

                    if (!AnnotationUtil.isMethodBean(method)) {
                        continue;
                    }

                    this.registerBean(method.getReturnType(), new MethodBean<>(method));

                }
            }
        }
    }

    public List<BeanHolder<?>> getBeans() {
        return this.beans.entrySet()
                .stream()
                .map(BeanHolder::fromEntry)
                .collect(Collectors.toList());
    }

    public record BeanHolder<T>(Class<? extends T> beanClass, Bean<T> bean) {

        public static <T> BeanHolder<T> fromEntry(Map.Entry<Class<?>, Bean<?>> entry) {
            return new BeanHolder<>(
              ReflectionUtil.unsafeCast(entry.getKey()),
              ReflectionUtil.unsafeCast(entry.getValue())
            );
        }
    }
}
