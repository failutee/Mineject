package task;

@FunctionalInterface
public interface TaskService<T> {

    void runTaskTimer(ScheduledTask<T> scheduledTask, long delay, long repeat, boolean async);

}
