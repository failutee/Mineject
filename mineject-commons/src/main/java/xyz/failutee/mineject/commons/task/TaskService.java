package xyz.failutee.mineject.commons.task;

@FunctionalInterface
public interface TaskService<T> {

    void runTaskTimer(ScheduledTask<T> scheduledTask, long delay, long repeat, boolean async);

}
