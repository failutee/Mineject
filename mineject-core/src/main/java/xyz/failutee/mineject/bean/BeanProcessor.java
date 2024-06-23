package xyz.failutee.mineject.bean;

import xyz.failutee.mineject.processor.AnnotedProcessor;
import xyz.failutee.mineject.processor.AnnotedProcessorFunction;
import xyz.failutee.mineject.processor.Processor;
import xyz.failutee.mineject.util.ReflectionUtil;

import java.lang.annotation.Annotation;
import java.util.*;

public class BeanProcessor {

    private final Map<Class<?>, Set<Processor<?>>> processorRegistry = new HashMap<>();

    public <A extends Annotation, T> BeanProcessor onProcess(Class<A> annotationClass, Class<? extends T> typeClass, AnnotedProcessorFunction<A, T> processorFunction) {
        AnnotedProcessor<A, T> processor = new AnnotedProcessor<>(annotationClass, processorFunction);
        return this.onProcess(typeClass, processor);
    }

    public <T> BeanProcessor onProcess(Class<? extends T> typeClass, Processor<T> processor) {
        this.processorRegistry.computeIfAbsent(typeClass, k -> new HashSet<>()).add(processor);
        return this;
    }

    public boolean isProcessed(Class<?> clazz) {
        if (this.processorRegistry.containsKey(clazz)) {
            return true;
        }

        return this.processorRegistry.keySet().stream().anyMatch(keyClass -> keyClass.isAssignableFrom(clazz));
    }

    private <T> List<Class<? extends T>> findProcessorKey(Class<T> clazz) {
        List<Class<? extends T>> keys = new ArrayList<>();

        for (Class<?> keyClass : this.processorRegistry.keySet()) {
            if (keyClass.isAssignableFrom(clazz)) {
                keys.add(ReflectionUtil.unsafeCast(keyClass));
            }
        }
        return keys;
    }

    public <T> void processBean(Class<? extends T> clazz, T instance) {
        if (!this.isProcessed(clazz)) {
            return;
        }

        List<Class<? extends T>> keyClasses = ReflectionUtil.unsafeCast(this.findProcessorKey(clazz));

        for (Class<? extends T> keyClass : keyClasses) {
            Set<Processor<T>> processors = ReflectionUtil.unsafeCast(this.processorRegistry.get(keyClass));

            processors.forEach(processor -> processor.process(instance));
        }
    }
}
