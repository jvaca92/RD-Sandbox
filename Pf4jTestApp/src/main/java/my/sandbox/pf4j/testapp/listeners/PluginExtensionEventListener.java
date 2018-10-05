package my.sandbox.pf4j.testapp.listeners;

import my.sandbox.pf4j.spring.ExtensionRegistry;
import my.sandbox.pf4j.spring.PluginManager;
import my.sandbox.pf4j.spring.events.PluginDisabledEvent;
import org.pf4j.PluginWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class PluginExtensionEventListener {

    private static final Logger LOG = LoggerFactory.getLogger(PluginExtensionEventListener.class);

    private final PluginManager pluginManager;
    private final ExtensionRegistry extensionRegistry;

    @Autowired
    public PluginExtensionEventListener(PluginManager pluginManager, ExtensionRegistry extensionRegistry) {
        this.pluginManager = pluginManager;
        this.extensionRegistry = extensionRegistry;
    }

    /** Event base on plugin disabled event which will
     * remove all extensions related to plugin from the extension register
     * **/
    @EventListener
    private void unregisterDisabledPluginExtensions(PluginDisabledEvent event) {
        PluginWrapper plugin = event.getPluginStateEvent().getPlugin();

        Set<String> extensionClassNames = plugin.getPluginManager().getExtensionClassNames(plugin.getPluginId());
        if(extensionClassNames != null && extensionClassNames.isEmpty()) {
            extensionClassNames
                    .forEach(className -> {
                        try {
                            Class<?> extensionClass = plugin.getPluginClassLoader().loadClass(className);
                            extensionRegistry.unregisterExtension(extensionClass);
                        } catch (ClassNotFoundException e) {
                            LOG.error("Could not found class {} of plugin {}", className, plugin.getPluginId());
                        }
                    });
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initializePluginsWithExtensions() {
        pluginManager.loadPlugins();
        pluginManager.startPlugins();

        Set<String> extensionClassNames = pluginManager.getExtensionClassNames(null);
        for (String extensionClassName : extensionClassNames) {
            try {
                LOG.info("Register extension '{}' as bean", extensionClassName);
                Class<?> extensionClass = getClass().getClassLoader().loadClass(extensionClassName);
                Object instance = pluginManager.getExtensionFactory().create(extensionClass);
                extensionRegistry.registerExtension(extensionClass, instance);
            } catch (ClassNotFoundException e) {
                LOG.error(e.getMessage(), e);
            }
        }

        // add extensions for each started plugin
        List<PluginWrapper> startedPlugins = pluginManager.getStartedPlugins();
        for (PluginWrapper plugin : startedPlugins) {
            LOG.debug("Registering extensions of the plugin '{}' as beans", plugin.getPluginId());
            extensionClassNames = pluginManager.getExtensionClassNames(plugin.getPluginId());
            for (String extensionClassName : extensionClassNames) {
                try {
                    LOG.debug("Register extension '{}' as bean", extensionClassName);
                    Class<?> extensionClass = plugin.getPluginClassLoader().loadClass(extensionClassName);
                    Object instance = pluginManager.getExtensionFactory().create(extensionClass);
                    extensionRegistry.registerExtension(extensionClass, instance);
                } catch (ClassNotFoundException e) {
                    LOG.error("Class not found: {}", e);
                }
            }
        }
    }


}
