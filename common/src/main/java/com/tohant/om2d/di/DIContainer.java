package com.tohant.om2d.di;

import com.tohant.om2d.di.annotation.*;
import com.tohant.om2d.di.exception.DependencyNotFoundException;
import com.tohant.om2d.di.exception.DependencyResolutionException;
import lombok.Getter;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Dependency injection container
 */
public class DIContainer {

    @Getter
    private final Map<Class<?>, Object> singletonCache = new ConcurrentHashMap<>();
    private final Set<Class<?>> singletonClasses = new HashSet<>();
    private final Set<Class<?>> classes = new HashSet<>();
    private final Map<Class<?>, Object> processedClasses = new HashMap<>();
    private final ConfigSource configSource;

    public DIContainer(Class<?> clazz) throws InvocationTargetException, IllegalAccessException, IOException {
        configSource = new YamlConfigSource("game.yml", clazz);
        Set<Object> registered = componentScan(clazz.getPackageName());

        PostConstructProcessor postConstructProcessor = new PostConstructProcessor();
        for (Object o : registered) {
            Arrays.stream(o.getClass().getMethods())
                    .filter(m -> m.isAnnotationPresent(PostConstruct.class))
                    .forEach(postConstructProcessor::addBean);
        }
        postConstructProcessor.processPostConstructMethods();
    }

    public Set<Object> componentScan(String basePackage) throws IOException {
        classes.addAll(ClassScannerUtil.getClasses(basePackage));
        final Set<Object> registered = new HashSet<>();
        for (Class<?> clazz : classes) {
            if (hasAnyBeanAnnotation(clazz)) {
                registered.add(resolve(clazz));
            }
        }
        return registered;
    }

    public <T> T resolve(Class<T> clazz) {
        if (singletonCache.containsKey(clazz)) {
            return clazz.cast(singletonCache.get(clazz));
        } else {
            if (hasAnyBeanAnnotation(clazz) && isSingleton(clazz)) {
                singletonClasses.add(clazz);
            }
            System.out.println(clazz);
            T instance = createInstance(clazz);
            processPostConstructMethods(instance);
            return instance;
        }
    }

    private <T> T createInstance(Class<T> clazz) {
        try {
            if (processedClasses.containsKey(clazz)) {
                return (T) processedClasses.get(clazz);
            }

            Constructor<?> constructor = clazz.getDeclaredConstructors()[0];
            if (!Modifier.isPublic(constructor.getModifiers())) {
                constructor.setAccessible(true);
            }

            Object[] args = Arrays.stream(constructor.getParameterTypes())
                    .map(parameterClass -> {
                        if (hasAnyBeanAnnotation(parameterClass)) {
                            Object instance;
                            if (isSingleton(parameterClass)) {
                                instance = resolve(parameterClass);
                            } else {
                                instance = createInstance(parameterClass);
                            }
                            processedClasses.put(parameterClass, instance);
                            return instance;
                        } else if (parameterClass.isInterface() || Modifier.isAbstract(parameterClass.getModifiers())) {
                            Set<Class<?>> implementations = classes.stream()
                                    .filter(candidate -> parameterClass.isAssignableFrom(candidate) && !Modifier.isAbstract(candidate.getModifiers()))
                                    .collect(Collectors.toSet());

                            if (!implementations.isEmpty()) {
                                if (implementations.size() > 1) {
                                    throw new DependencyResolutionException("Multiple implementations found for interface/abstract class: " + parameterClass.getName(), null);
                                }

                                Class<?> implementationClass = implementations.iterator().next();
                                return createInstance(implementationClass);
                            } else {
                                throw new DependencyNotFoundException("No implementation found for interface/abstract class: " + parameterClass.getName());
                            }
                        }
                        throw new DependencyNotFoundException("No found dependency: " + parameterClass);
                    })
                    .toArray();

            T instance = (T) constructor.newInstance(args);
            if (isSingleton(clazz)) {
                singletonCache.put(clazz, instance);
            }

            return instance;
        } catch (InstantiationException e) {
            throw new DependencyResolutionException("Cannot instantiate interface: " + clazz.getName(), e);
        } catch (Exception e) {
            throw new DependencyResolutionException("Error creating instance of " + clazz.getName(), e);
        }
    }


    private <T> void processPostConstructMethods(T instance) {
        Class<?> clazz = instance.getClass();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(PostConstruct.class)) {
                try {
                    method.setAccessible(true);
                    method.invoke(instance);
                } catch (Exception e) {
                    throw new DependencyResolutionException("Error invoking @PostConstruct method " + method.getName() +
                            " in class " + clazz.getName(), e);
                }
            }
        }
    }

    private boolean isSingleton(Class<?> implementation) {
        return (implementation.isAnnotationPresent(Component.class) &&
                implementation.getAnnotation(Component.class).value() == BeanType.SINGLETON)
                || (implementation.isAnnotationPresent(Configuration.class) &&
                implementation.getAnnotation(Configuration.class).value() == BeanType.SINGLETON)
                || (implementation.isAnnotationPresent(Dao.class) &&
                implementation.getAnnotation(Dao.class).value() == BeanType.SINGLETON);
    }

    private boolean hasAnyBeanAnnotation(Class<?> clazz) {
        return clazz.isAnnotationPresent(Component.class) || clazz.isAnnotationPresent(Configuration.class)
                || clazz.isAnnotationPresent(Dao.class);
    }

}