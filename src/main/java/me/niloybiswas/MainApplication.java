package me.niloybiswas;

import me.niloybiswas.controllers.HomeServlet;
import me.niloybiswas.controllers.ProductController;
import me.niloybiswas.controllers.ProductServlet;
import me.niloybiswas.controllers.SearchServlet;
import me.niloybiswas.repositories.ProductRepository;
import me.niloybiswas.services.ProductService;
import me.niloybiswas.services.SearchService;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;

import java.io.File;

public class MainApplication {
    public static void main(String[] args) throws LifecycleException {
        Tomcat tomcat = new Tomcat();
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
        tomcat.getServer().await();
    }
}