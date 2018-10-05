package my.sandbox.pf4j.testapp;

import javafx.application.Application;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import my.sandbox.pf4j.testapp.controllers.events.PluginActionEvent;
import my.sandbox.pf4j.testapp.controllers.events.PluginActionEventHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.awt.*;

@SpringBootApplication
public class SampleApplication extends Application {

    private ConfigurableApplicationContext springContext;
    private Parent rootNode;
    private Stage stage;

    public static void main(final String[] args) {
        SampleApplication.launch(args);
    }

    @Override
    public void init() throws Exception {
        springContext = SpringApplication.run(SampleApplication.class);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/PluginRepository.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean);
        rootNode = fxmlLoader.load();
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(rootNode));
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        springContext.close();
    }
}
