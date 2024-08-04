package xyz.failutee.mineject.bean;

import xyz.failutee.mineject.processor.AnnotedProcessor;
import xyz.failutee.mineject.processor.AnnotedProcessorFunction;
import xyz.failutee.mineject.processor.Processor;
import xyz.failutee.mineject.util.CollectionUtil;
import xyz.failutee.mineject.util.ReflectionUtil;

import java.lang.annotation.Annotation;
import java.util.*;

public class BeanProcessor {

    private final Map<Class<?>, Set<Processor<?>>> processorRegistry = new HashMap<>();

    public <A extends Annotation, T> BeanProcessor onProcess(Class<A> annotationClass, Class<? extends T> typeClass, AnnotedProcessorFunction<A, T> function) {
        var processedAnnotation = new AnnotedProcessor<>(annotationClass, function);
        return this.onProcess(typeClass, processedAnnotation);
    }

    public <T> BeanProcessor onProcess(Class<? extends T> typeClass, Processor<T> processor) {
        this.processorRegistry.computeIfAbsent(typeClass, k -> new HashSet<>()).add(processor);
        return this;
    }

    public boolean isProcessed(Class<?> clazz) {
        return CollectionUtil.isAnyClassAssignableFrom(this.processorRegistry.keySet(), clazz);
    }

    public <T> List<Class<? extends T>> findProcessorKey(Class<T> clazz) {
        List<Class<? extends T>> keys = new ArrayList<>();

        for (Class<?> keyClass : this.processorRegistry.keySet()) {
            if (keyClass.isAssignableFrom(clazz)) {
                keys.add(ReflectionUtil.unsafeCast(keyClass));
            }
        }
        return keys;
    }

    public <T> Set<Processor<T>> getProcessors(Class<? extends T> clazz) {
        List<Class<? extends T>> keyClasses = ReflectionUtil.unsafeCast(this.findProcessorKey(clazz));

        Set<Processor<T>> processors = new HashSet<>();

        for (Class<? extends T> keyClass : keyClasses) {
            var classProcessor = this.processorRegistry.get(keyClass);
            processors.addAll(ReflectionUtil.unsafeCast(classProcessor));
        }

        return processors;
    }
}
