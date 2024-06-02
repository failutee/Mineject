package xyz.failutee.mineject.processor;

import java.util.Optional;
import java.util.Set;

public class ClassProcessorManager {

    private final Set<ClassProcessor> processedClasses;

    protected ClassProcessorManager(Set<ClassProcessor> processedClasses) {
        this.processedClasses = processedClasses;
    }

    public Optional<ClassProcessor> getClassProcessor(Class<?> clazz) {
        return this.processedClasses.stream()
                .filter(processor -> processor.shouldProcess(clazz))
                .findFirst();
    }
}