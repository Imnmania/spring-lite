package me.niloybiswas.spring_lite;

import jakarta.servlet.http.HttpServlet;
import me.niloybiswas.spring_lite.annotations.Component;
import me.niloybiswas.spring_lite.annotations.Value;
import org.apache.catalina.Context;
import org.apache.catalina.Host;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import java.io.File;

@Component
public class TomcatConfig {
    private static Tomcat tomcat;
    private static Context context;
    private static final String contextPath = "";

    @Value(key = "${server.port}")
    private String serverPort;

    public TomcatConfig() {}

    protected void initTomcat() {
        tomcat = new Tomcat();
        tomcat.setPort(Integer.parseInt(serverPort));
        tomcat.getConnector();
        String docBase = new File(".").getAbsolutePath();
        context = tomcat.addContext(contextPath, docBase);
    }

    protected void start() throws LifecycleException {
        Host host = tomcat.getHost();
        host.setName("localhost");
        host.setAppBase("webapps");
        tomcat.start();
        System.out.println("\u001B[34m" + "[STARTED] \uD83C\uDF3F Spring Lite started on Port: " + Integer.parseInt(serverPort) + "\u001B[0m");
    }

    protected void registerServlet(Object instance, Class<?> clazz, String urlMapping) {
        tomcat.addServlet(contextPath, clazz.getSimpleName(), (HttpServlet) instance);
        context.addServletMappingDecoded(urlMapping, clazz.getSimpleName());
    }
}
