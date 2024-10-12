package me.niloybiswas;

import me.niloybiswas.spring_lite.SpringLiteApplication;
import me.niloybiswas.spring_lite.annotations.PackageScan;
import org.apache.catalina.LifecycleException;

import java.lang.reflect.InvocationTargetException;

@PackageScan(scanPackages = {"me.niloybiswas"})
public class MainApplication {
    public static void main(String[] args) throws LifecycleException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        /*Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir("tmp");
        Connector connector = new Connector();
        connector.setPort(9999);
        tomcat.setConnector(connector);

        String contextPath = "";
        String documentBase = new File(".").getAbsolutePath();
        Context context = tomcat.addContext(contextPath, documentBase);

        // creating objects and injecting dependencies
        // repositories
        ProductRepository productRepository = new ProductRepository();
        // services
        ProductService productService = new ProductService(productRepository);
        SearchService searchService = new SearchService(productService);
        // controllers
        ProductController productController = new ProductController(productService, searchService);
        // servlets
        HomeServlet homeServlet = new HomeServlet();
        ProductServlet productServlet = new ProductServlet(productController);
        SearchServlet searchServlet = new SearchServlet(productController);

        // settings routes and contexts
        // home servlet
        tomcat.addServlet(contextPath, "HomeServlet", homeServlet);
        context.addServletMappingDecoded("/", "HomeServlet");
        // product servlet
        tomcat.addServlet(contextPath, "ProductServlet", productServlet);
        context.addServletMappingDecoded("/products/*", "ProductServlet");
        // search servlet
        tomcat.addServlet(contextPath, "SearchServlet", searchServlet);
        context.addServletMappingDecoded("/products/search", "SearchServlet");

        // start tomcat
        tomcat.start();
        tomcat.getServer().await();*/

        // custom spring
        /*String packageName = "me.niloybiswas";
        ClassLoader classLoader = MainApplication.class.getClassLoader();

        URL resource = classLoader.getResource(Utils.convertPackageToPath(packageName));
        System.out.println("resource = " + resource);

        File resourceFile = new File(resource.getPath());
        List<Class<?>> classes = Utils.getRecursiveClasses(resourceFile.getAbsolutePath(), packageName);
        for(Class<?> clazz : classes) {
            System.out.println("clazz.getName() = " + clazz.getName());
            // creating a new instance of every class
            Object newObject = clazz.getDeclaredConstructor().newInstance();
        }*/

        SpringLiteApplication.run(MainApplication.class);
    }
}