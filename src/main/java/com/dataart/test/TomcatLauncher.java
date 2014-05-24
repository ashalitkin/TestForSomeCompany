package com.dataart.test;

import com.dataart.test.action.TestFacade;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.apache.tomcat.util.descriptor.web.ContextResource;

import javax.servlet.ServletException;
import java.io.File;
import java.net.MalformedURLException;

/**
 * Created by andrey on 23/05/2014.
 */
public class TomcatLauncher {
    public static void main(String[] args) throws LifecycleException, ServletException, MalformedURLException {
        final Tomcat tomcat = new Tomcat();
        tomcat.setPort(6660);
        String webDirectory = new File("./src/main/webapp").getAbsolutePath();
        Context context = tomcat.addWebapp("/", new File(webDirectory).getAbsolutePath());

        File additionWebInfClasses = new File("target/classes");
        WebResourceRoot resources = new StandardRoot(context);
        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes", additionWebInfClasses.getAbsolutePath(), "/"));
        context.setResources(resources);
        //context.setConfigFile(new File("./src/main/resources/context.xml").toURI().toURL());

        ContextResource resource = new ContextResource();
        resource.setName("jdbc/dbtest");
        resource.setAuth("Container");
        resource.setType("javax.sql.DataSource");
        resource.setScope("Sharable");
        resource.setProperty("driverClassName",
                "com.mysql.jdbc.Driver");
        resource.setProperty("url", "jdbc:mysql://localhost/testDataArt");
        resource.setProperty("username", "root");
        resource.setProperty("password", "root");

        context.getNamingResources().addResource(resource);

        tomcat.enableNaming();
        tomcat.start();
        tomcat.getServer().await();
    }
}
