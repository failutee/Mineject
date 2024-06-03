package task;

@FunctionalInterface
public interface ScheduledTask<T> {

    void runTask(T task);

}
