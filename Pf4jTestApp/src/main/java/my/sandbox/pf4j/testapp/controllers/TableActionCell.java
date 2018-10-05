package my.sandbox.pf4j.testapp.controllers;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.*;
import my.sandbox.pf4j.spring.PluginManager;
import my.sandbox.pf4j.testapp.controllers.events.PluginActionEvent;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.function.Function;

public class TableActionCell extends TableCell<PluginItem, Boolean> {

    //Menu button for cell
    private final MenuButton actionMenu = new MenuButton("Actions");
    //Actions
    private final MenuItem enableAction = new MenuItem("Enable");
    private final MenuItem disableAction = new MenuItem("Disable");
    private final MenuItem removeAction = new MenuItem("Remove");
    private final PluginManager pluginManager;

    public TableActionCell(PluginManager pluginManager, TableView<PluginItem> tableView) {
        this.pluginManager = pluginManager;
        enableAction.setOnAction(event -> {
                String id = tableView.getItems().get(this.getIndex()).getPluginID();
                pluginManager.enablePlugin(id);
                pluginManager.startPlugin(id);
        });
//                Event.fireEvent(Event.NULL_SOURCE_TARGET,
//                new PluginActionEvent(new EventType<>(MessageFormat.format("{0}_{1}", PluginActionEvent.ENABLE_EVENT, UUID.randomUUID().toString())), this.getIndex())));
        disableAction.setOnAction(event -> {
            String id = tableView.getItems().get(this.getIndex()).getPluginID();
                    pluginManager.stopPlugin(id);
                    pluginManager.disablePlugin(id);
                });
//                Event.fireEvent(Event.NULL_SOURCE_TARGET,
//                new PluginActionEvent(new EventType<>(MessageFormat.format("{0}_{1}", PluginActionEvent.DISABLE_EVENT, UUID.randomUUID().toString())), this.getIndex())));
        removeAction.setOnAction(event -> {
            String id = tableView.getItems().get(this.getIndex()).getPluginID();
            pluginManager.stopPlugin(id);
            pluginManager.deletePlugin(id);
            pluginManager.unloadPlugin(id);
            tableView.getItems().remove(this.getIndex());
        });
//                Event.fireEvent(Event.NULL_SOURCE_TARGET,
//                new PluginActionEvent(new EventType<>(MessageFormat.format("{0}_{1}", PluginActionEvent.REMOVE_EVENT, UUID.randomUUID().toString())), this.getIndex())));
        actionMenu.getItems().addAll(Arrays.asList(enableAction, disableAction, removeAction));
    }

    @Override
    protected void updateItem(Boolean item, boolean empty) {
        super.updateItem(item, empty);
        if (!empty) {
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            setGraphic(actionMenu);
        } else {
            setGraphic(null);
        }
    }
}
