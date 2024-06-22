package xyz.failutee.mineject.platform;

import xyz.failutee.mineject.processor.ProcessorConfigurer;

public interface InjectionPlatform {

    InjectionPlatform EMPTY_PLATFORM = new EmptyInjectionPlatform();

    ProcessorConfigurer getProcessorConfigurer();

    static boolean isEmpty(InjectionPlatform platform) {
         return platform.equals(EMPTY_PLATFORM);
    }
}
