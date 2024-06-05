package xyz.failutee.mineject.commons.task;

@FunctionalInterface
public interface ScheduledTask<T> {

    void runTask(T task);

}
