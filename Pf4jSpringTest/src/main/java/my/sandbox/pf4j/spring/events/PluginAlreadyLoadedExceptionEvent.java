package my.sandbox.pf4j.spring.events;

import org.pf4j.PluginAlreadyLoadedException;
import org.springframework.context.ApplicationEvent;

public class PluginAlreadyLoadedExceptionEvent extends ApplicationEvent {

    private PluginAlreadyLoadedException pluginAlreadyLoadedException;

    public PluginAlreadyLoadedExceptionEvent(Object source, PluginAlreadyLoadedException pluginAlreadyLoadedException) {
        super(source);
        this.pluginAlreadyLoadedException = pluginAlreadyLoadedException;
    }

    public PluginAlreadyLoadedException getPluginAlreadyLoadedException() {
        return pluginAlreadyLoadedException;
    }
}

