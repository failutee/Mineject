package xyz.failutee.mineject.processor;

@FunctionalInterface
public interface Processor<T> {

    void process(T instance);

}
