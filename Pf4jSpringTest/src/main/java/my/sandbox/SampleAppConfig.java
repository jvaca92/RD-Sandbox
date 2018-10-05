package my.sandbox;


import my.sandbox.pf4j.spring.ExtensionRegistry;
import my.sandbox.pf4j.spring.events.PluginStateEventListenerDelegate;
import my.sandbox.pf4j.spring.ExtensionManager;
import my.sandbox.pf4j.spring.PluginManager;
import org.picocontainer.PicoBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.net.URISyntaxException;

@Configuration
@ComponentScan("my.sandbox.pf4j.spring.*")
public class SampleAppConfig {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Bean
    public PluginManager pluginManager() throws URISyntaxException {
        PluginManager springPluginManager = new PluginManager("F:\\plugins");
        springPluginManager.addPluginStateListener(pluginStateEventListenerDelegate());
        springPluginManager.setApplicationContext(context);
        return springPluginManager;
    }

    @Bean
    public ExtensionManager extensionManager() throws URISyntaxException {
        ExtensionManager extensionManager = ExtensionManager.getInstance();
        extensionManager.setPluginManager(pluginManager());
        extensionManager.setExtensionRegistry(extensionContainer());
        return extensionManager;
    }

    @Bean
    public ExtensionRegistry extensionContainer() {
        //TODO more configuration for PicoContainer
        return new ExtensionRegistry(new PicoBuilder()
                .withLocking()
                .build());
    }

    @Bean
    public PluginStateEventListenerDelegate pluginStateEventListenerDelegate() {
        return new PluginStateEventListenerDelegate(eventPublisher);
    }
}
