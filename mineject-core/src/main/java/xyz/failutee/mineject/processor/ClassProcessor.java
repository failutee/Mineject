package xyz.failutee.mineject.processor;

public interface ClassProcessor {

    boolean shouldProcess(Class<?> clazz);

    void processClass(Object instance);

}
