package my.sandbox.pf4j.testapp.listeners;

import my.sandbox.pf4j.spring.events.PluginAlreadyLoadedExceptionEvent;
import org.pf4j.PluginAlreadyLoadedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class PluginExceptionListeners {

    private static final Logger LOG = LoggerFactory.getLogger(PluginExceptionListeners.class);

    @EventListener
    private void handlePluginAlreadyLoadedException(PluginAlreadyLoadedExceptionEvent event) {
       LOG.info("Was invoked PluginAlreadyLoadedExceptionEvent for plugin {}", event.getPluginAlreadyLoadedException().getPluginId());
       //TODO do update of the plugin if exist
    }
}
