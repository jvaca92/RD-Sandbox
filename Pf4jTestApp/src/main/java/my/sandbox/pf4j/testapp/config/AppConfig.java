package my.sandbox.pf4j.testapp.config;


import my.sandbox.pf4j.spring.ExtensionManager;
import my.sandbox.pf4j.spring.ExtensionRegistry;
import my.sandbox.pf4j.spring.PluginManager;
import my.sandbox.pf4j.spring.events.PluginStateEventListenerDelegate;
import my.sandbox.pf4j.testapp.jersey.JerseyConfig;
import my.sandbox.pf4j.testapp.jersey.JerseyResourceManager;
import org.glassfish.jersey.servlet.ServletContainer;
import org.picocontainer.PicoBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.net.URISyntaxException;

@Configuration
//@EnableWebMvc
@ComponentScan(basePackages = "my.sandbox.pf4j.testapp.*")
public class AppConfig {

    @Autowired
    private ApplicationContext context;

    @Value("pf4j.plugins.storage")
    private String pluginStorageDir;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Bean
    public JerseyResourceManager jerseyResourceManager() {
        return new JerseyResourceManager();
    }

//    @Bean
//    public ServletRegistrationBean apiV1() {
//        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
//        DispatcherServlet dispatcherServlet = new DispatcherServlet();
//        dispatcherServlet.setApplicationContext(context);
//        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(dispatcherServlet, "/rest/*");
//        servletRegistrationBean.setName("api-v1");
//
//        return servletRegistrationBean;
//    }

    //Register Jersey Sevrlet
    @Bean
    public ServletRegistrationBean jerseyServlet() throws URISyntaxException {
        ServletRegistrationBean registration = new ServletRegistrationBean(new ServletContainer(new JerseyConfig(jerseyResourceManager())));
        //registration.addInitParameter(ServletProperties.JAXRS_APPLICATION_CLASS, JerseyConfig.class.getName());
        registration.addUrlMappings("/rest/*");
        registration.setLoadOnStartup(1);
        return registration;
    }

    @Bean
    public PluginManager pluginManager() throws URISyntaxException {
        PluginManager springPluginManager = new PluginManager(pluginStorageDir);
        springPluginManager.addPluginStateListener(pluginStateEventListenerDelegate());
        springPluginManager.setApplicationContext(context);
        springPluginManager.setEventPublisher(eventPublisher);
        return springPluginManager;
    }

    @Bean
    public ExtensionManager extensionManager() throws URISyntaxException {
        ExtensionManager extensionManager = ExtensionManager.getInstance();
        extensionManager.setPluginManager(pluginManager());
        extensionManager.setExtensionRegistry(extensionContainer());
        return extensionManager;
    }

    @Bean
    public ExtensionRegistry extensionContainer() {
        //TODO more configuration for PicoContainer
        return new ExtensionRegistry(new PicoBuilder()
                .withLocking()
                .build());
    }

    @Bean
    public PluginStateEventListenerDelegate pluginStateEventListenerDelegate() {
        return new PluginStateEventListenerDelegate(eventPublisher);
    }
}
