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

    public <T> Set<Bean<T>> collectBeans(Class<? extends T> beanClass) {
        return this.beans.entrySet().stream()
                .map(BeanHolder::fromEntry)
                .filter(holder -> beanClass.isAssignableFrom(holder.beanClass))
                .map(holder -> ReflectionUtil.<Bean<T>>unsafeCast(holder.bean))
                .collect(Collectors.toSet());
    }

    public void collectBeans(Collection<Class<?>> classes) {
        for (Class<?> clazz : classes) {

            for (Class<?> innerClass : clazz.getDeclaredClasses()) {
                this.handleBeanClass(innerClass);
            }

            this.handleBeanClass(clazz);
        }
    }

    private void handleBeanClass(Class<?> clazz) {
        if (this.beanProcessor.isProcessed(clazz)) {
            var processors = this.beanProcessor.getProcessors(clazz);

            if (!processors.isEmpty()) {
                this.registerBean(clazz, new ProcessedBean<>(clazz, ReflectionUtil.unsafeCast(processors)));
            }
        }

        if (AnnotationUtil.isComponent(clazz)) {
            this.registerBean(clazz, new ComponentBean<>(clazz));
        }

        if (AnnotationUtil.isBeanSetup(clazz)) {
            this.registerBean(clazz, new ComponentBean<>(clazz));

            for (Method method : clazz.getDeclaredMethods()) {

                if (!AnnotationUtil.isMethodBean(method)) {
                    continue;
                }

                this.registerBean(method.getReturnType(), new MethodBean<>(clazz, method));

            }
        }
    }

    public List<BeanHolder<?>> getBeans() {
        return this.beans.entrySet()
                .stream()
                .map(BeanHolder::fromEntry)
                .collect(Collectors.toList());
    }

    public record BeanHolder<T>(Class<? extends T> beanClass, Bean<?> bean) {

        public static <T> BeanHolder<T> fromEntry(Map.Entry<Class<?>, Bean<?>> entry) {
            return new BeanHolder<>(
              ReflectionUtil.unsafeCast(entry.getKey()),
              ReflectionUtil.unsafeCast(entry.getValue())
            );
        }
    }
}
