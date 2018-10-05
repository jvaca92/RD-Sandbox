package my.sandbox.pf4j.spring;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pf4j.PluginWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestAppConfig.class)
public class FXPluginManagerTest {

    private static final Logger LOG = LoggerFactory.getLogger(FXPluginManagerTest.class);

    public FXPluginManagerTest() {
    }

    @Autowired
    PluginManager pluginManager;

    @Autowired
    ExtensionRegistry extensionRegistry;


    /** Please configure the right storage of plugins ***/
    @Test
    public void loadPluginTest() throws Exception {

        LOG.debug("Starting loading plugin");
        String pluginID = "";//pluginManager.loadPlugin(Paths.get(getClass().getResource("/plugins/plugin-test.jar").toURI()));

        LOG.debug("Plugin root is {}", System.getProperty("pf4j.pluginsDir"));
        LOG.debug("Result of loading {}", pluginID);
        pluginManager.loadPlugins();
        PluginWrapper pluginWrapper = pluginManager.getPlugin("test-plugin");
        pluginWrapper.getPlugin().start();


        LOG.debug("Starting plugin with ID {}", pluginID);
        Set<String> extensions = pluginManager.getExtensionClassNames(pluginWrapper.getPluginId());
        ClassLoader pluginClassLoader = pluginWrapper.getPluginClassLoader();
        ExtensionManager extensionManager = ExtensionManager.getInstance();

        extensions.forEach(
                extension -> {
                    LOG.debug("Plugin with ID {} has extension {}", extension);
                    try {
                        Class<?> extensionClass = pluginClassLoader.loadClass(extension);
                        Object newInstance = extensionManager.getExtension(extensionClass);
                        LOG.debug("Service from ExtensionRegistry {}", newInstance.getClass().getName());
                        Assert.assertEquals(newInstance, extensionRegistry.getRegisteredExtension(extensionClass));
                        LOG.debug("The value of method sayHello is {}", newInstance.getClass().getMethod("printMessage").invoke(newInstance));
                    } catch (ClassNotFoundException e) {
                        LOG.error(e.getMessage());
                    } catch (NoSuchMethodException e) {
                        LOG.error(e.getMessage());
                    } catch (IllegalAccessException e) {
                        LOG.error(e.getMessage());
                    } catch (InvocationTargetException e) {
                        LOG.error(e.getMessage());
                    }
                });

//        Assert.isInstanceOf(String.class, pluginID);
    }
}