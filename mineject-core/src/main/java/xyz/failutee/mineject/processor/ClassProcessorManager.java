package xyz.failutee.mineject.processor;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

public class ClassProcessorManager {

    private final Set<ClassProcessor> processedClasses = new HashSet<>();

    public ClassProcessorManager(Consumer<ClassProcessorManager> managerConsumer) {
        managerConsumer.accept(this);
    }

    public void registerProcessor(ClassProcessor classProcessor) {
        this.processedClasses.add(classProcessor);
    }

    public Optional<ClassProcessor> getClassProcessor(Class<?> clazz) {
        return this.processedClasses.stream()
                .filter(processor -> processor.shouldProcess(clazz))
                .findFirst();
    }
}