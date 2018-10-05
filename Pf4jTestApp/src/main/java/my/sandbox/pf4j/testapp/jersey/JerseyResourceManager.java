package my.sandbox.pf4j.testapp.jersey;

import javafx.util.Pair;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spi.Container;
import org.glassfish.jersey.server.spi.ContainerLifecycleListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.*;
import java.util.stream.Collectors;

public class JerseyResourceManager {

    private static final Logger LOG = LoggerFactory.getLogger(JerseyResourceManager.class);

    private volatile Container container;
    private Map<String, List<Object>> registeredResources = Collections.synchronizedMap(new HashMap<>());

    public synchronized void unregisterResources(String moduleKey) {
        registeredResources.remove(moduleKey);
        reloadContext();
    }

    public synchronized void registerResource(String moduleKey, List<Object> resources) {
        registeredResources.putIfAbsent(moduleKey, resources);
        reloadContext();
    }

    public synchronized void registerResources(Map<String, List<Object>> modules) {
        modules.forEach((k, v) -> registeredResources.putIfAbsent(k, v));
        reloadContext();
    }

    private ResourceConfig createResourceConfig() {
        ResourceConfig rc = new ResourceConfig();
        //this.context = createWebApplicationContext();
        //rc.property(CONTEXT_CONFIG_VALUE, this.context);
        //allResources.forEach(singleton -> registerSingleton(singleton.getClass().getName(), singleton));
        getSetOfResources().forEach(resource -> rc.register(resource));
        return rc;
    }

    private void reloadContext() {
        final ResourceConfig rc = createResourceConfig();
        this.container.reload(rc);
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    public Set<Object> getSetOfResources() {
        return  registeredResources.values()
                .stream()
                .flatMap(components -> components.stream())
                .collect(Collectors.toSet());
    }
}
