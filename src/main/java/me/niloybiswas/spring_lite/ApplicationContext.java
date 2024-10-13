package me.niloybiswas.spring_lite;

import me.niloybiswas.spring_lite.annotations.Autowired;
import me.niloybiswas.spring_lite.annotations.Component;
import me.niloybiswas.spring_lite.annotations.Servlet;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplicationContext {
    private final Map<String, Object> beanFactory = new HashMap<>();
    private final int serverPort = 9999;
    private final TomcatConfig tomcatConfig;

    private static ApplicationContext instance;

    private ApplicationContext() {
        tomcatConfig = new TomcatConfig(serverPort);
    }

    public static synchronized ApplicationContext getInstance() {
        if (instance == null) {
            return new ApplicationContext();
        }
        return instance;
    }

    protected void initContainers(List<Class<?>> classes) throws Exception {
        createBeans(classes);
        injectDependencies(classes);
        registerServlets(classes);
        tomcatConfig.start(serverPort);
    }

    protected void createBeans(List<Class<?>> classes) throws Exception {
        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(Component.class) || clazz.isAnnotationPresent(Servlet.class)) {
                Object instance = clazz.getDeclaredConstructor().newInstance();
                beanFactory.put(getBeanName(clazz), instance);
            }
        }
    }

    protected void injectDependencies(List<Class<?>> classes) throws IllegalAccessException {
        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(Component.class) || clazz.isAnnotationPresent(Servlet.class)) {
                Object clazzBean = getBean(getBeanName(clazz));

                Field[] declaredFields = clazz.getDeclaredFields();
                for (Field field : declaredFields) {
                    if (field.isAnnotationPresent(Autowired.class)) {
                        Object dependentBean = getBean(field.getName().toLowerCase());
                        field.setAccessible(true);
                        field.set(clazzBean, dependentBean);
                    }
                }
            }
        }
    }

    protected void registerServlets(List<Class<?>> classes) {
        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(Servlet.class)) {
                Servlet servlet = clazz.getAnnotation(Servlet.class);
                Object instance = getBean(getBeanName(clazz));
                TomcatConfig.registerServlet(instance, clazz, servlet.urlMapping());
            }
        }
    }

    private Object getBean(String name) {
        return beanFactory.get(name.toLowerCase());
    }

    private String getBeanName(Class<?> clazz) {
        String[] parts = clazz.getName().split("\\.");
        return parts[parts.length - 1].toLowerCase();
    }
}
