package xyz.failutee.mineject.dependency;

import xyz.failutee.mineject.bean.Bean;
import xyz.failutee.mineject.bean.BeanService;
import xyz.failutee.mineject.exception.DependencyException;
import xyz.failutee.mineject.util.ReflectionUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Parameter;
import java.util.Optional;

public class DependencyResolverImpl implements DependencyResolver {

    private final BeanService beanService;

    public DependencyResolverImpl(BeanService beanService) {
        this.beanService = beanService;
    }

    @Override
    public <T> T createInstance(Class<T> clazz) {
        Constructor<T> constructor = ReflectionUtil.findConstructor(clazz, clazz.getDeclaredConstructors());

        constructor.setAccessible(true);

        Object[] args = this.resolveArguments(constructor);

        try {
            return ReflectionUtil.createNewInstance(constructor, args);
        } catch (Exception exception) {
            throw new DependencyException("There was a problem while creating an instance of the '%s' class.".formatted(clazz.getSimpleName()), exception);
        }
    }

    @Override
    public Object[] resolveArguments(Executable executable, int skip) {
        Parameter[] parameters = executable.getParameters();
        Object[] args = new Object[parameters.length];

        for (int i = skip; i < parameters.length; i++) {

            Class<?> type = parameters[i].getType();

            args[i] = this.getOrInitialize(type);

        }

        return args;
    }

    @Override
    public Object[] resolveArguments(Executable executable) {
        return this.resolveArguments(executable, 0);
    }

    @Override
    public <T> T getOrInitialize(Class<? extends T> beanClass) {
        Optional<Bean<T>> optionalBean = this.beanService.getBean(beanClass);

        if (optionalBean.isPresent()) {
            Bean<T> bean = optionalBean.get();

            if (!bean.isInitialized()) {
                bean.handleBean(this);
            }

            return bean.getInstance();
        }

        throw new RuntimeException("Bean '%s' not found, did you forgot to provide it?".formatted(beanClass.getSimpleName()));
    }
}
