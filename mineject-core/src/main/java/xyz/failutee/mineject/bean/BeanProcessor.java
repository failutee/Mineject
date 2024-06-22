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

    public <T> void processBean(Class<? extends T> clazz, T instance) {
        Set<Processor<T>> processors = ReflectionUtil.unsafeCast(this.processorRegistry.get(clazz));
        
        if (processors == null) {
            return;
        }

        processors.forEach(processor -> processor.process(instance));
    }
}
