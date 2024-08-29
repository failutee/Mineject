package xyz.failutee.example.spigot.processor;

import xyz.failutee.example.spigot.annotation.Service;
import xyz.failutee.mineject.bean.BeanProcessor;
import xyz.failutee.mineject.processor.ProcessorConfigurer;

public class ExamplePluginProcessorConfigurer implements ProcessorConfigurer {

    @Override
    public void configureProcessor(BeanProcessor beanProcessor) {
        beanProcessor.onProcess(Service.class, Object.class, (service, object) -> {
           /*
            * Empty...
            * All classes with the @Service annotation will behave like @Component.
            * I created it only for visual reasons, so that classes called
            * ...Service would be marked as @Service with this annotation
           */
        });
    }
}
