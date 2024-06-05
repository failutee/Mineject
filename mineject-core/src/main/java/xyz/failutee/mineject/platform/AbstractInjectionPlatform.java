package xyz.failutee.mineject.platform;

import xyz.failutee.mineject.processor.ClassProcessorManager;

import java.util.function.Consumer;

public abstract class AbstractInjectionPlatform implements InjectionPlatform {

    private final ClassProcessorManager classProcessorManager;

    public AbstractInjectionPlatform() {
        this.classProcessorManager = new ClassProcessorManager(this.configureProcessors());
    }

    public abstract Consumer<ClassProcessorManager> configureProcessors();

    @Override
    public ClassProcessorManager getClassProcessorManager() {
        return this.classProcessorManager;
    }
}
