package xyz.failutee.mineject.bean.impl;

import xyz.failutee.mineject.bean.AbstractBean;
import xyz.failutee.mineject.dependency.DependencyResolver;
import xyz.failutee.mineject.processor.AnnotedProcessor;
import xyz.failutee.mineject.processor.Processor;

import java.util.Set;

public class ProcessedBean<T> extends AbstractBean<T> {

    private final Set<Processor<T>> processors;

    public ProcessedBean(Set<Processor<T>> processors) {
        this.processors = processors;
    }

    @Override
    public void handleBean(Class<? extends T> beanClass, DependencyResolver dependencyResolver) {
        for (Processor<T> processor : this.processors) {
            if (processor instanceof AnnotedProcessor<?,?> annotedProcessor) {
                if (!beanClass.isAnnotationPresent(annotedProcessor.getAnnotationType())) {
                    return;
                }
            }

            if (!this.isInitialized()) {
                T instance = dependencyResolver.createInstance(beanClass);
                this.initializeBean(instance);
            }

            processor.process(this.getInstance());
        }
    }
}
