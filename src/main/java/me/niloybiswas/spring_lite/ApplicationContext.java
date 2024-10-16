package me.niloybiswas.spring_lite;

import me.niloybiswas.spring_lite.annotations.*;
import me.niloybiswas.spring_lite.core.Environment;
import me.niloybiswas.spring_lite.core.Utils;
import org.apache.catalina.LifecycleException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplicationContext {
    private final Map<String, Object> beanFactory = new HashMap<>();

    private TomcatConfig tomcatConfig;

    private static ApplicationContext instance;

    private ApplicationContext() {}

    public static synchronized ApplicationContext getInstance() {
        if (instance == null) {
            instance = new ApplicationContext();
            return instance;
        }
        return instance;
    }

    protected void initContainers(List<Class<?>> classes) throws Exception {
        createBeans(classes);
        injectDependencies(classes);
        injectEnvironmentValues(classes);
        initTomcatConfig(classes);
        registerDispatcherServlet(classes);
        startTomcatServer();
    }

    private void createBeans(List<Class<?>> classes) throws Exception {
        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(Component.class) || clazz.isAnnotationPresent(Servlet.class)
                    || clazz.isAnnotationPresent(RestController.class) || clazz.isAnnotationPresent(Service.class)) {
                Object instance = clazz.getDeclaredConstructor().newInstance();
                beanFactory.put(getBeanName(clazz), instance);
                System.out.println(Utils.getPartialGreenText("[Bean Created]: ") + clazz.getCanonicalName());
            }
        }
    }

    private void injectDependencies(List<Class<?>> classes) throws IllegalAccessException {
        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(Component.class) || clazz.isAnnotationPresent(Servlet.class)
                    || clazz.isAnnotationPresent(RestController.class) || clazz.isAnnotationPresent(Service.class)) {
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

    private void injectEnvironmentValues(List<Class<?>> classes) throws IllegalAccessException, RuntimeException {
        Environment environment = (Environment) getBean(Environment.class, classes);
        if (environment == null) {
            throw new RuntimeException("Environment could not be found!!!");
        }
        for (Class<?> clazz : classes) {
            Object bean = getBean(getBeanName(clazz));
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field field : declaredFields) {
                if (field.isAnnotationPresent(Value.class)) {
                    Value valueAnnotation = field.getAnnotation(Value.class);
                    field.setAccessible(true);
                    field.set(bean, environment.getProperty(valueAnnotation.key()));
                }
            }
        }
    }

    private List<ControllerMethod> findControllerMethods(List<Class<?>> classes) throws Exception {
        List<ControllerMethod> controllerMethods = new ArrayList<>();
        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(RestController.class)) {
                String basePath = "";
                if (clazz.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
                    basePath = requestMapping.url();
                }
                for (Method method : clazz.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(RequestMapping.class)) {
                        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                        ControllerMethod controllerMethod = ControllerMethod.builder()
                                .clazz(clazz)
                                .instance(getBean(getBeanName(clazz)))
                                .method(method)
                                .methodType(requestMapping.type())
                                .url(basePath + requestMapping.url())
                                .build();
                        System.out.println("[REQUEST_MAPPING] = " + Utils.getPartialBlueText(controllerMethod.getUrl()));
                        controllerMethods.add(controllerMethod);
                    }
                    if (method.isAnnotationPresent(GetMapping.class)) {
                        GetMapping getMapping = method.getAnnotation(GetMapping.class);
                        ControllerMethod controllerMethod = ControllerMethod.builder()
                                .clazz(clazz)
                                .instance(getBean(getBeanName(clazz)))
                                .method(method)
                                .methodType(getMapping.methodType())
                                .url(basePath + getMapping.url())
                                .build();
                        System.out.println(Utils.getPartialGreenText("[GET]: ") + Utils.getPartialBlueText(controllerMethod.getUrl()));
                        controllerMethods.add(controllerMethod);
                    }
                    if (method.isAnnotationPresent(PostMapping.class)) {
                        PostMapping postMapping = method.getAnnotation(PostMapping.class);
                        ControllerMethod controllerMethod = ControllerMethod.builder()
                                .clazz(clazz)
                                .instance(getBean(getBeanName(clazz)))
                                .method(method)
                                .methodType(postMapping.methodType())
                                .url(basePath + postMapping.url())
                                .build();
                        System.out.println(Utils.getPartialGreenText("[POST]: ") + Utils.getPartialBlueText(controllerMethod.getUrl()));
                        controllerMethods.add(controllerMethod);
                    }
                    if (method.isAnnotationPresent(PutMapping.class)) {
                        PutMapping putMapping = method.getAnnotation(PutMapping.class);
                        ControllerMethod controllerMethod = ControllerMethod.builder()
                                .clazz(clazz)
                                .instance(getBean(getBeanName(clazz)))
                                .method(method)
                                .methodType(putMapping.methodType())
                                .url(basePath + putMapping.url())
                                .build();
                        System.out.println(Utils.getPartialGreenText("[PUT]: ") + Utils.getPartialBlueText(controllerMethod.getUrl()));
                        controllerMethods.add(controllerMethod);
                    }
                    if (method.isAnnotationPresent(DeleteMapping.class)) {
                        DeleteMapping deleteMapping = method.getAnnotation(DeleteMapping.class);
                        ControllerMethod controllerMethod = ControllerMethod.builder()
                                .clazz(clazz)
                                .instance(getBean(getBeanName(clazz)))
                                .method(method)
                                .methodType(deleteMapping.methodType())
                                .url(basePath + deleteMapping.url())
                                .build();
                        System.out.println(Utils.getPartialGreenText("[DELETE]: ") + Utils.getPartialBlueText(controllerMethod.getUrl()));
                        controllerMethods.add(controllerMethod);
                    }
                }
            }
        }
        return controllerMethods;
    }

    private void initTomcatConfig(List<Class<?>> classes) {
        tomcatConfig = (TomcatConfig) getBean(TomcatConfig.class, classes);
        if (tomcatConfig == null) {
            Utils.printRedText("[Tomcat] TomcatConfig bean not be found");
            throw new RuntimeException("TomcatConfig could not be found!!!");
        }
        tomcatConfig.initTomcat();
    }

    private void startTomcatServer() {
        try {
            tomcatConfig.start();
        } catch (LifecycleException e) {
            Utils.printRedText("[Tomcat] Tomcat could not be started!!!");
            throw new RuntimeException(e);
        }
    }

    private void registerDispatcherServlet(List<Class<?>> classes) throws Exception {
        DispatcherServlet dispatcherServlet = new DispatcherServlet(findControllerMethods(classes));
        tomcatConfig.registerServlet(dispatcherServlet, dispatcherServlet.getClass(), "/");
    }

    private Object getBean(String name) {
        return beanFactory.get(name.toLowerCase());
    }

    private String getBeanName(Class<?> clazz) {
        String[] parts = clazz.getName().split("\\.");
        return parts[parts.length - 1].toLowerCase();
    }

    private Object getBean(Class<?> targetClass, List<Class<?>> classes) {
        for (Class<?> clazz : classes) {
            if (clazz.getTypeName().equals(targetClass.getTypeName())) {
                return beanFactory.get(getBeanName(clazz));
            }
        }
        return null;
    }
}
