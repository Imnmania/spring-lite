package me.niloybiswas.spring_lite.server;

import jakarta.servlet.http.HttpServlet;
import org.apache.catalina.Context;
import org.apache.catalina.Host;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import java.io.File;

public class TomcatConfig {
    private static Tomcat tomcat;
    private static Context context;

    private static final String contextPath = "";

    public static void initTomcat() throws LifecycleException {
        int port = 9999;
        tomcat = new Tomcat();
        tomcat.setPort(port);
        tomcat.getConnector();

        Host host = tomcat.getHost();
        host.setName("localhost");
        host.setAppBase("webapps");;

        tomcat.start();
        System.out.println("[STARTED] Spring Lite started on Port: " + port);

        String docBase = new File(".").getAbsolutePath();
        context = tomcat.addContext(contextPath, docBase);
    }

    public static void registerServlet(Object instance, Class<?> clazz, String urlMapping) {
        tomcat.addServlet(contextPath, clazz.getSimpleName(), (HttpServlet) instance);
        context.addServletMappingDecoded(urlMapping, clazz.getSimpleName());
    }
}
