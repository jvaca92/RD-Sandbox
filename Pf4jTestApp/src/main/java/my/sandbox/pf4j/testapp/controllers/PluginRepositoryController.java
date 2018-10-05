package my.sandbox.pf4j.testapp.controllers;

import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import my.sandbox.pf4j.spring.Plugin;
import my.sandbox.pf4j.spring.PluginManager;
import my.sandbox.pf4j.spring.events.*;
import my.sandbox.pf4j.spring.xml.descriptors.RestModuleDescriptor;
import my.sandbox.pf4j.testapp.jersey.JerseyResourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.*;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
public class PluginRepositoryController implements Initializable {

    private static final Logger LOG = LoggerFactory.getLogger(PluginRepositoryController.class);

    @Autowired
    private PluginManager pluginManager;

    @Autowired
    private JerseyResourceManager jerseyResourceManager;

    @Autowired
    private ApplicationContext applicationContext;

    //Controls
    private FileChooser fileChooser;

    @FXML
    private TableView tv_pluginList;

    @FXML
    private ProgressIndicator pi_pluginUploadProcess;

    @FXML
    private TableColumn tc_pluginDesc;

    @FXML
    private TableColumn tc_pluginActions;

    @FXML
    private TableColumn tc_pluginID;

    @FXML
    private void handlePluginLoadAction(ActionEvent event) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        // jerseyResourceManager.registerResources("test-module", Collections.singletonList(new TestEndpoint()));
        //LOG.info("New resource was registered....");
        fileChooser = new FileChooser();
        configureFileChooser(fileChooser);
        File file = fileChooser.showOpenDialog(new Stage());

        //Verify is the plugin is not null
        if(file != null) {
            pi_pluginUploadProcess.setVisible(Boolean.TRUE);
            CompletableFuture.runAsync(() -> {
            LOG.debug("Was chosen file with URI {}", file.toURI().getPath());
            String pluginID = pluginManager.loadPlugin(Paths.get(file.toURI()));
            LOG.debug("Was found plugin with id {}", pluginID);
            Plugin plugin = (Plugin) pluginManager.getPlugin(pluginID).getPlugin();
            registerAllResources(plugin);
            /*** Just for test purpose ***/
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            pluginManager.startPlugin(pluginID);
            LOG.info("Was started plugin with ID  {}", pluginID);

            Platform.runLater(() -> {
                tv_pluginList.getItems().addAll(PluginItem.newBuilder()
                        .pluginID(new SimpleStringProperty(plugin.getWrapper().getPluginId()))
                        .pluginDesc(new SimpleStringProperty(plugin.getWrapper().getDescriptor().getPluginDescription()))
                        .build());
            });
            }).thenAccept(aVoid ->  Platform.runLater(() -> { pi_pluginUploadProcess.setVisible(Boolean.FALSE); }));
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tc_pluginID.setCellValueFactory(new PropertyValueFactory<>("pluginID"));
        tc_pluginDesc.setCellValueFactory(new PropertyValueFactory<>("pluginDesc"));
        // define a simple boolean cell value for the action column so that the column will only be shown for non-empty rows.
        tc_pluginActions.setCellValueFactory((Callback<TableColumn.CellDataFeatures<PluginItem, Boolean>, ObservableValue<Boolean>>) features -> new SimpleBooleanProperty(features.getValue() != null));
        // create a cell value factory with an add button for each row in the table.
        tc_pluginActions.setCellFactory((Callback<TableColumn<PluginItem, Boolean>, TableCell<PluginItem, Boolean>>) PluginItemBooleanTableColumn -> new TableActionCell(pluginManager, tv_pluginList));

    }

    private void configureFileChooser(final FileChooser fileChooser) {
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JAR", "*.jar")
        );
    }

    /*** Listener to listen in which state the plugin is ***/
//    @EventListener
//    private void runPIPluginUploadProcess(PluginResolvedEvent createdEvent) {
//            this.pi_pluginUploadProcess.setVisible(Boolean.TRUE);
//    }
//
//    @EventListener
//    private void runPIPluginUploadProcess(PluginStartedEvent startedEvent) {
//            this.pi_pluginUploadProcess.setVisible(Boolean.FALSE);
//    }

    @EventListener
    private void disablePluginModuleListener(PluginStoppedEvent stoppedEvent) {
         Plugin plugin = (Plugin) pluginManager.getPlugin(stoppedEvent.getPluginStateEvent().getPlugin().getPluginId()).getPlugin();
         plugin.getModulesDescriptor().getRestModuleDescriptor()
                 .forEach(module -> jerseyResourceManager.unregisterResources(module.getKey()));
    }

    @EventListener
    private void enablePluginModuleListener(PluginStartedEvent startedEvent) {
        registerAllResources((Plugin) startedEvent.getPluginStateEvent().getPlugin().getPlugin());
    }

    private void registerAllResources(Plugin plugin) {
        ClassLoader classLoader = pluginManager.getPlugin(plugin.getWrapper().getPluginId()).getPluginClassLoader();
        Map<String, List<Object>> allResources = plugin.getModulesDescriptor()
                .getRestModuleDescriptor()
                .stream()
                .collect(Collectors.toMap(RestModuleDescriptor::getKey, module ->
                        module.getClazz()
                                .stream()
                                .map( clazz -> {
                                    try {
                                        Class<?> clazzResource = classLoader.loadClass(module.getClazz().get(0));
                                        LOG.debug("Was loaded class with class name {}", clazzResource.getName());
                                        Object resourceInstance = clazzResource.newInstance();
                                        LOG.debug("The rest clazz was autowired as bean");
                                        plugin.getApplicationContext().getAutowireCapableBeanFactory().autowireBean(resourceInstance);
                                        return resourceInstance;
                                    } catch (Exception e) {
                                        LOG.error("Error during loading plugin: {}", e);
                                    }
                                    return null;
                                }).collect(Collectors.toList())
                ));
        jerseyResourceManager.registerResources(allResources);
    }

}
