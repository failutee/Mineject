package xyz.failutee.mineject.dependency;

import xyz.failutee.mineject.annotation.BeanSetup;
import xyz.failutee.mineject.annotation.Component;
import xyz.failutee.mineject.bean.*;
import xyz.failutee.mineject.bean.impl.ComponentBean;
import xyz.failutee.mineject.bean.impl.MethodBean;

import java.lang.reflect.Method;
import java.util.Set;

public class DependencyComponents {

    private final Set<Class<?>> classes;
    private final DependencyResolver dependencyResolver;
    private final BeanProcessor beanProcessor;
    private final BeanService beanService;

    public DependencyComponents(
        Set<Class<?>> classes,
        DependencyResolver dependencyResolver,
        BeanProcessor beanProcessor,
        BeanService beanService
    ) {
        this.classes = classes;
        this.dependencyResolver = dependencyResolver;
        this.beanProcessor = beanProcessor;
        this.beanService = beanService;
    }

    public void collectBeans() {
        for (Class<?> clazz : this.classes) {

            if (clazz.isAnnotationPresent(Component.class)) {
                this.beanService.registerBean(clazz, new ComponentBean<>());
            }

            if (clazz.isAnnotationPresent(BeanSetup.class)) {
                this.beanService.registerBean(clazz, new ComponentBean<>());

                for (Method method : clazz.getDeclaredMethods()) {

                    if (!method.isAnnotationPresent(xyz.failutee.mineject.annotation.Bean.class)) {
                        continue;
                    }

                    this.beanService.registerBean(method.getReturnType(), new MethodBean<>(method));

                }
            }

        }
    }

//    public void processPlatform() {
//        for (Class<?> clazz : this.classes) {
//
//            if (!this.beanProcessor.isProcessed(clazz)) {
//                continue;
//            }
//
//            var processors = this.beanProcessor.getProcessors(clazz);
//
//            Object instance = null;
//
//            for (var processor : processors) {
//
//                if (processor instanceof AnnotedProcessor<?,?> annotedProcessor) {
//                    if (!clazz.isAnnotationPresent(annotedProcessor.getAnnotationType())) {
//                        continue;
//                    }
//                }
//
//                if (instance == null) {
////                    instance = this.dependencyResolver.getOrCreateBean(clazz);
//                }
//
//                processor.process(ReflectionUtil.unsafeCast(instance));
//
//            }
//        }
//    }
}
