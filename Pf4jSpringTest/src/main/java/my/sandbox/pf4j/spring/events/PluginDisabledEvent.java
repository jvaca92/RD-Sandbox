package my.sandbox.pf4j.spring.events;

import org.pf4j.PluginStateEvent;
import org.springframework.context.ApplicationEvent;

public class PluginDisabledEvent extends ApplicationEvent {

    private  PluginStateEvent pluginStateEvent;

    public PluginDisabledEvent(Object source, PluginStateEvent pluginStateEvent) {
        super(source);
        this.pluginStateEvent = pluginStateEvent;
    }

    public PluginStateEvent getPluginStateEvent() {
        return pluginStateEvent;
    }
}
