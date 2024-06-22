package xyz.failutee.mineject.platform;

import xyz.failutee.mineject.processor.ProcessorConfigurer;

public class EmptyInjectionPlatform implements InjectionPlatform {

    @Override
    public ProcessorConfigurer getProcessorConfigurer() {
        return ignore -> {};
    }
}
