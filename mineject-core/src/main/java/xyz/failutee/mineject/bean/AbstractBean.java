package xyz.failutee.mineject.bean;

public abstract class AbstractBean<T> implements Bean<T> {

    private T instance;

    @Override
    public boolean isInitialized() {
        return this.instance != null;
    }

    @Override
    public void initializeBean(T instance) {
        this.instance = instance;
    }

    @Override
    public T getInstance() {
        return this.instance;
    }
}
