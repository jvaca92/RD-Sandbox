package my.sandbox.fxml.viewmesh;

import javafx.application.Application;
import javafx.stage.Stage;
import my.sandbox.fxml.viewmesh.utils.StreamUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.VelocimacroFactory;
import org.apache.velocity.runtime.VelocimacroManager;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.apache.velocity.runtime.resource.loader.StringResourceLoader;
import org.apache.velocity.runtime.resource.util.StringResourceRepository;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;

public class App {

    public static void main(String[] args) throws IOException, XMLStreamException {

        VelocityEngine engine = new VelocityEngine();
        FXMLContentProcessor contentProcessor = new FXMLContentProcessor();
        StringWriter writer = new StringWriter();
        //engine.setProperty(VelocityEngine.VM_LIBRARY,"/decorator-macro.vm");
        engine.setProperty(RuntimeConstants.RESOURCE_LOADER, "string");
        engine.setProperty("classpath.resource.loader.class", StringResourceLoader.class.getName());
        engine.init();

        StringResourceRepository repository = (StringResourceRepository) engine.getApplicationAttribute(StringResourceLoader.REPOSITORY_NAME_DEFAULT);
        //repository.getStringResource("decorator-macro", StreamUtils.readInputStreamAsString(Class.class.getResourceAsStream()))

        VelocityContext context = new VelocityContext();
        context.put("decorators", contentProcessor.getDecorators());

        Template tmp = engine.getTemplate("/decorators/template2.vm");
        tmp.merge(context, writer, Collections.singletonList("decorator-macro.vm"));


        System.out.print(writer.toString());
    }
}
