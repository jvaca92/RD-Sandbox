package my.sandbox.pf4j.testapp.controllers.events;

import javafx.event.Event;
import javafx.event.EventType;

public class PluginActionEvent extends Event {

    public static final String ENABLE_EVENT = "plugin-enable-event";
    public static final String DISABLE_EVENT = "plugin-enable-event";
    public static final String REMOVE_EVENT = "plugin-enable-event";

    private int index;

    public PluginActionEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }

    public PluginActionEvent(EventType<? extends Event> eventType, int index) {
        super(eventType);
        this.index = index;
    }

    public static String isEvent(String name) {
        String event = name.split("_")[0];
        if(event.equals(DISABLE_EVENT)) return DISABLE_EVENT;
        else if(event.equals(REMOVE_EVENT)) return REMOVE_EVENT;
        else if(event.equals(ENABLE_EVENT)) return ENABLE_EVENT;
        else return null;
    }

    public int getIndex() {
        return index;
    }
}
