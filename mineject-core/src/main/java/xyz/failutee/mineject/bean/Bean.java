package xyz.failutee.mineject.bean;

import java.util.function.Supplier;

public class Bean<T> {

    private T instance;

    private final Class<? extends T> instanceClass;
    private final Supplier<T> instanceSupplier;

    public Bean(Class<? extends T> instanceClass, Supplier<T> instanceSupplier) {
        this.instanceClass = instanceClass;
        this.instanceSupplier = instanceSupplier;
    }

    public Class<? extends T> getInstanceClass() {
        return this.instanceClass;
    }

    public T getInstance() {
        if (this.instanceSupplier != null) {
            this.instance = this.instanceSupplier.get();
        }
        return this.instance;
    }
}