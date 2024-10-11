package me.niloybiswas;

import me.niloybiswas.core.Utils;
import org.apache.catalina.LifecycleException;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Objects;


public class MainApplication {
    public static void main(String[] args) throws LifecycleException {
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
        String packageName = "me.niloybiswas";
        ClassLoader classLoader = MainApplication.class.getClassLoader();

        URL resource = classLoader.getResource(Utils.convertPackageToPath(packageName));
        System.out.println("resource = " + resource);

        File resourceFile = new File(Objects.requireNonNull(resource).getPath());
        /*File[] files = resourceFile.listFiles();
        for (File file : files) {
            System.out.println("file.getName() = " + file.getName());
        }*/
        List<String> files = Utils.recursiveFiles(resourceFile.getAbsolutePath());
        for (String file : files) {
            System.out.println("file = " + file);
        }
    }


}