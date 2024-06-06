package xyz.failutee.mineject.platform;

import xyz.failutee.mineject.processor.ClassProcessorManager;

import java.util.function.Consumer;

public class EmptyInjectionPlatform extends AbstractInjectionPlatform {

    @Override
    public Consumer<ClassProcessorManager> configureProcessors() {
        return (ignore) -> { /* ... */ };
    }
}
