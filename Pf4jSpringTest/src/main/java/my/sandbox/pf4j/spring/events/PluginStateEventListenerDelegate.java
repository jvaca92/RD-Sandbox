package my.sandbox.pf4j.spring.events;

import org.pf4j.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

/**
 * Delegate plugins event which are fired during plugin lifecycle by plugin manager
 */

public class PluginStateEventListenerDelegate implements PluginStateListener {

    private static final Logger LOG = LoggerFactory.getLogger(PluginStateEventListenerDelegate.class);

    private final ApplicationEventPublisher eventPublisher;

    public PluginStateEventListenerDelegate(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void pluginStateChanged(PluginStateEvent pluginStateEvent) {

        PluginState state = pluginStateEvent.getPluginState();
        PluginWrapper plugin = pluginStateEvent.getPlugin();

        if(state.equals(PluginState.CREATED)) {
            eventPublisher.publishEvent(new PluginCreatedEvent(this, pluginStateEvent));
            LOG.info(String.format("The plugin %s was created", plugin.getPluginId()));
        } else if(state.equals(PluginState.RESOLVED)) {
            eventPublisher.publishEvent(new PluginResolvedEvent(this, pluginStateEvent));
            LOG.info(String.format("The plugin %s was resolved", plugin.getPluginId()));
        } else if(state.equals(PluginState.DISABLED)) {
            eventPublisher.publishEvent(new PluginDisabledEvent(this, pluginStateEvent));
            LOG.info(String.format("The plugin %s was disabled", plugin.getPluginId()));
        } else if(state.equals(PluginState.STARTED)) {
            eventPublisher.publishEvent(new PluginStartedEvent(this, pluginStateEvent));
            LOG.info(String.format("The plugin %s was started", plugin.getPluginId()));
        } else if(state.equals(PluginState.STOPPED)) {
            eventPublisher.publishEvent(new PluginStoppedEvent(this, pluginStateEvent));
            LOG.info(String.format("The plugin %s was stopped", plugin.getPluginId()));
        }
    }
}
