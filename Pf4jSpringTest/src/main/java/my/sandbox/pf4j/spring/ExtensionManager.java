package my.sandbox.pf4j.spring;

import org.pf4j.PluginManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ExtensionManager {

    private static final Logger LOG = LoggerFactory.getLogger(ExtensionManager.class);

    private PluginManager pluginManager;
    private ExtensionRegistry extensionRegistry;

    private ExtensionManager() {
    }

    private static class Holder {
        private static final ExtensionManager INSTANCE = new ExtensionManager();
    }

    public <T> T getExtension(Class<?> extensionClazz) {
        if(!extensionRegistry.containsExtensionOfType(extensionClazz)) {
            Object extension = pluginManager.getExtensionFactory().create(extensionClazz);
            extensionRegistry.registerExtension(extension.getClass(), extension);
            return (T) extension;
        }
        return extensionRegistry.getRegisteredExtension(extensionClazz);
    }

    public static ExtensionManager getInstance() {
        return Holder.INSTANCE;
    }

    public PluginManager getPluginManager() {
        return pluginManager;
    }

    public void setPluginManager(PluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

    public void setExtensionRegistry(ExtensionRegistry extensionRegistry) {
        this.extensionRegistry = extensionRegistry;
    }
}
