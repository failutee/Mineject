package xyz.failutee.mineject.processor;

import java.util.HashSet;
import java.util.Set;

public class ClassProcessorManagerFactory {

    private final Set<ClassProcessor> processedClasses = new HashSet<>();

    public ClassProcessorManagerFactory processor(ClassProcessor processor) {
        this.processedClasses.add(processor);
        return this;
    }

    public ClassProcessorManager build() {
        return new ClassProcessorManager(this.processedClasses);
    }
}
