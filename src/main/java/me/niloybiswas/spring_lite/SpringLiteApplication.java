package me.niloybiswas.spring_lite;

import me.niloybiswas.spring_lite.annotations.PackageScan;
import me.niloybiswas.spring_lite.core.ClassScanner;

import java.net.URL;
import java.util.*;

public class SpringLiteApplication {

    private static final Map<String, Object> beanFactory = new HashMap<>();

    public static ApplicationContext run(Class<?> mainClass) throws Exception {
        ApplicationContext applicationContext = ApplicationContext.getInstance();
        final List<Class<?>> classes = getClasses(mainClass);
        applicationContext.initContainers(classes);
        return applicationContext;
    }

    private static List<Class<?>> getClasses(Class<?> mainClass) throws ClassNotFoundException {
        PackageScan packageScan = mainClass.getAnnotation(PackageScan.class);
        ClassLoader classLoader = SpringLiteApplication.class.getClassLoader();
        List<Class<?>> classes = new ArrayList<>();
        for (String packageName : packageScan.scanPackages()) {
            URL resource = classLoader.getResource(packageName.replace('.', '/'));
            String filePath = resource.getPath();
            classes.addAll(ClassScanner.getRecursiveClasses(filePath, packageName));
        }
        for (Class<?> clazz : classes) {
            System.out.println("Class loaded => " + clazz.getName());
        }
        return classes;
    }
}
