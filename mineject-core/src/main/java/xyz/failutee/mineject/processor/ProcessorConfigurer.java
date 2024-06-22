package xyz.failutee.mineject.processor;

import xyz.failutee.mineject.bean.BeanProcessor;

@FunctionalInterface
public interface ProcessorConfigurer {

    void configureProcessor(BeanProcessor beanProcessor);

}
