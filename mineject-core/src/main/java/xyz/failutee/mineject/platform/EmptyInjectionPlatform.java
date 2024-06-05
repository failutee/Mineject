package xyz.failutee.mineject.platform;

import xyz.failutee.mineject.processor.ClassProcessorManager;
import xyz.failutee.mineject.processor.ClassProcessorManagerFactory;

public class EmptyInjectionPlatform implements InjectionPlatform {

    @Override
    public ClassProcessorManager getClassProcessorManager() {
        return new ClassProcessorManagerFactory().build();
    }
}
