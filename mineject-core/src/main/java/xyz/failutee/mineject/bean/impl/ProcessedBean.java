package xyz.failutee.mineject.bean.impl;

import xyz.failutee.mineject.bean.AbstractBean;
import xyz.failutee.mineject.dependency.DependencyResolver;
import xyz.failutee.mineject.processor.Processor;

import java.util.Set;

public class ProcessedBean<T> extends AbstractBean<T> {

    private final Set<Processor<T>> processors;

    public ProcessedBean(Class<? extends T> beanClass, Set<Processor<T>> processors) {
        super(beanClass);
        this.processors = processors;
    }

    @Override
    public void handleBean(DependencyResolver dependencyResolver) {
        for (Processor<T> processor : this.processors) {

            if (!this.isInitialized()) {
                T instance = dependencyResolver.createInstance(this.getBeanClass());
                this.initializeBean(instance);
            }

            processor.process(this.getInstance());
        }
    }
}
