package xyz.failutee.mineject.platform;

import xyz.failutee.mineject.processor.ClassProcessorManager;

@FunctionalInterface
public interface InjectionPlatform {

    InjectionPlatform EMPTY_PLATFORM = new EmptyInjectionPlatform();

    ClassProcessorManager getClassProcessorManager();

     static boolean isEmpty(InjectionPlatform platform) {
         return platform.equals(EMPTY_PLATFORM);
    }

}
