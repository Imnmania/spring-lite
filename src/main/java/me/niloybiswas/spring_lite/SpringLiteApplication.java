package me.niloybiswas.spring_lite;

import me.niloybiswas.spring_lite.annotations.Autowired;
import me.niloybiswas.spring_lite.annotations.Component;
import me.niloybiswas.spring_lite.annotations.PackageScan;
import me.niloybiswas.spring_lite.annotations.Servlet;
import me.niloybiswas.spring_lite.core.ClassScanner;
import me.niloybiswas.spring_lite.core.Utils;
import me.niloybiswas.spring_lite.server.TomcatConfig;
import org.apache.catalina.LifecycleException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.*;

public class SpringLiteApplication {

    private static final Map<String, Object> beanFactory = new HashMap<>();

    public static void run(Class<?> mainClass) throws Exception {
        List<Class<?>> classes = loadClasses(mainClass);
        createBeans(classes);
        injectDependencies(classes);
        startServer();
        registerServlets(classes);
    }

    private static List<Class<?>> loadClasses(Class<?> mainClass) throws ClassNotFoundException {
        PackageScan packageScan = mainClass.getAnnotation(PackageScan.class);
        ClassLoader classLoader = SpringLiteApplication.class.getClassLoader();
        List<Class<?>> classes = new ArrayList<>();
        for (String packageName : packageScan.scanPackages()) {
            URL resource = classLoader.getResource(Utils.convertPackageToPath(packageName));
            String filePath = resource.getPath();
            System.out.println("packageName => " + packageName);
            System.out.println("filePath => " + filePath);
            classes.addAll(ClassScanner.getRecursiveClasses(filePath, packageName));
        }
        for (Class<?> clazz : classes) {
            System.out.println("Class loaded => " + clazz.getName());
        }
        return classes;
    }

    private static void createBeans(List<Class<?>> classes) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(Component.class) || clazz.isAnnotationPresent(Servlet.class)) {
                Object instance = clazz.getDeclaredConstructor().newInstance();
                beanFactory.put(getBeanName(clazz), instance);
            }
        }
    }

    private static void injectDependencies(List<Class<?>> classes) throws IllegalAccessException {
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

    private static void registerServlets(List<Class<?>> classes) {
        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(Servlet.class)) {
                Servlet servlet = clazz.getAnnotation(Servlet.class);
                Object instance = getBean(getBeanName(clazz));
                TomcatConfig.registerServlet(instance, clazz, servlet.urlMapping());
            }
        }
    }

    private static void startServer() throws LifecycleException {
        TomcatConfig.initTomcat();
    }

    public static Object getBean(String name) {
        return beanFactory.get(name.toLowerCase());
    }

    private static String getBeanName(Class<?> clazz) {
        String[] parts = clazz.getName().split("\\.");
        return parts[parts.length - 1].toLowerCase();
    }
}
