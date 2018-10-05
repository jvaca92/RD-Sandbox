package my.sandbox.pf4j.spring.events;

import org.pf4j.PluginStateEvent;
import org.pf4j.PluginWrapper;
import org.springframework.context.ApplicationEvent;

public class PluginCreatedEvent extends ApplicationEvent {

    private  PluginStateEvent pluginStateEvent;

    public PluginCreatedEvent(Object source, PluginStateEvent pluginStateEvent) {
        super(source);
        this.pluginStateEvent = pluginStateEvent;
    }

    public PluginStateEvent getPluginStateEvent() {
        return pluginStateEvent;
    }
}
