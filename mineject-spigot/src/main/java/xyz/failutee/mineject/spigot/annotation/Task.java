package xyz.failutee.mineject.spigot.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Task {

    boolean async() default false;

    long delay() default 0;

    long period() default 1;

    TimeUnit unit() default TimeUnit.SECONDS;

}
