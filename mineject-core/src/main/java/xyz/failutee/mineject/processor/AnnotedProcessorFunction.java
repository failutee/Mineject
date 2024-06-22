package xyz.failutee.mineject.processor;

import java.lang.annotation.Annotation;

@FunctionalInterface
public interface AnnotedProcessorFunction<A extends Annotation, T> {

    void process(A annotation, T instance);

}
