package xyz.failutee.mineject.spigot.processor;

import org.bukkit.scheduler.BukkitTask;
import xyz.failutee.mineject.commons.task.ScheduledTask;
import xyz.failutee.mineject.commons.task.TaskService;
import xyz.failutee.mineject.exception.DependencyException;
import xyz.failutee.mineject.processor.ClassProcessor;
import xyz.failutee.mineject.spigot.annotation.Task;

public class TaskProcessor implements ClassProcessor {

    private final TaskService<BukkitTask> taskService;

    public TaskProcessor(TaskService<BukkitTask> taskService) {
        this.taskService = taskService;
    }

    @Override
    public boolean shouldProcess(Class<?> clazz) {
        return clazz.isAnnotationPresent(Task.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void processClass(Object instance) {
        Task task = instance.getClass().getDeclaredAnnotation(Task.class);

        if (!(instance instanceof ScheduledTask<?> scheduledTask) || !BukkitTask.class.isAssignableFrom(scheduledTask.getClass())) {
            throw new DependencyException("Class '%s' does not implement ScheduledTask<BukkitTask>".formatted(instance.getClass().getSimpleName()));
        }

        this.taskService.runTaskTimer((ScheduledTask<BukkitTask>) scheduledTask, task.delay(), task.repeat(), task.async());
    }
}
