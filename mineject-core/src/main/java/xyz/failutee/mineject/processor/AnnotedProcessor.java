package xyz.failutee.mineject.processor;

import xyz.failutee.mineject.util.ReflectionUtil;

import java.lang.annotation.Annotation;

public class AnnotedProcessor<A extends Annotation, T> implements Processor<T> {

    private final Class<? extends A> annotationType;
    private final AnnotedProcessorFunction<A, T> processorFunction;

    public AnnotedProcessor(Class<? extends A> annotationType, AnnotedProcessorFunction<A, T> processorFunction) {
        this.annotationType = annotationType;
        this.processorFunction = processorFunction;
    }

    @Override
    public void process(T instance) {
        Class<?> clazz = instance.getClass();

        if (!clazz.isAnnotationPresent(this.annotationType)) {
            return;
        }

        A annotation = ReflectionUtil.unsafeCast(clazz.getDeclaredAnnotation(this.annotationType));

        this.processorFunction.process(annotation, instance);
    }
}
