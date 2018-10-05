package my.sandbox.pf4j.testapp.jersey;

import my.sandbox.pf4j.testapp.endpoints.TestEndpoint;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spi.Container;
import org.glassfish.jersey.server.spi.ContainerLifecycleListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.ws.rs.ApplicationPath;


public class JerseyConfig extends ResourceConfig {

    private static final Logger LOG = LoggerFactory.getLogger(JerseyConfig.class);

    public JerseyConfig(JerseyResourceManager jerseyResourceManager) {
        register(new ContainerLifecycleListener() {
          @Override
          public void onStartup(Container container) {
              jerseyResourceManager.setContainer(container);
              LOG.info("Jersey app was start up...");
          }
          @Override
          public void onReload(Container container) {
              LOG.info("Jersey app was reload...");
          }

          @Override
          public void onShutdown(Container container) {
              LOG.info("Jersey app was shut down...");
          }
        });
    }
}
