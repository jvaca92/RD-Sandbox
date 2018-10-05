package my.sandbox.pf4j.spring;

import my.sandbox.pf4j.spring.events.PluginAlreadyLoadedExceptionEvent;
import my.sandbox.pf4j.spring.utils.PluginUtils;
import my.sandbox.pf4j.spring.xml.PluginDescriptorProperties;
import my.sandbox.pf4j.spring.xml.PluginModulesDescriptorFinder;
import my.sandbox.pf4j.spring.xml.XmlPluginDescriptorFinder;
import my.sandbox.pf4j.spring.xml.XmlPluginModulesDescriptorFinder;
import my.sandbox.pf4j.spring.xml.descriptors.PluginDescriptorValidator;
import my.sandbox.pf4j.spring.xml.descriptors.PluginDescriptorValidatorImpl;
import org.pf4j.*;
import org.pf4j.spring.SpringExtensionFactory;
import org.pf4j.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.jar.JarFile;

import static my.sandbox.pf4j.spring.xml.PluginDescriptorProperties.DEFAULT_XML_DESCRIPTOR_FILE_NAME;
import static my.sandbox.pf4j.spring.xml.PluginDescriptorProperties.DEFAULT_XSD_PLUGIN_INFO_DESCRIPTOR_FILE_NAME;

public class PluginManager extends DefaultPluginManager implements ApplicationContextAware {

    private static final Logger LOG = LoggerFactory.getLogger(PluginManager.class);

    /** Root plugins dir key defined by PF4J **/
    private static final String ROOT_PLUGINS_DIR_KEY = "pf4j.pluginsDir";

    private ApplicationContext applicationContext;
    private ApplicationEventPublisher eventPublisher;

    public PluginManager() {
        initialize();
    }

    public PluginManager(String pluginsRootDir) {
       super(Paths.get(pluginsRootDir));
    }

    @Override
    protected ExtensionFactory createExtensionFactory() {
        return new SpringExtensionFactory(this);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void setEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public PluginModulesDescriptorFinder createPluginModulesDescriptorFinder() {
        return new XmlPluginModulesDescriptorFinder();
    }

    @Override
    protected org.pf4j.PluginFactory createPluginFactory() {
        return new PluginFactory();
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    protected ExtensionFinder createExtensionFinder() {
        return new ServiceExtensionFinder(this);
    }

    @Override
    protected PluginDescriptorFinder createPluginDescriptorFinder() {
        return new XmlPluginDescriptorFinder();
    }

    @Override
    protected PluginWrapper loadPluginFromPath(Path pluginPath) throws PluginException {
        PluginWrapper pluginWrapper = null;
        if(FileUtils.isJarFile(pluginPath)) {
            try (JarFile jar = new JarFile(pluginPath.toFile())) {
                 PluginDescriptorValidator validator = createPluginDescriptorValidator();
                 boolean isValid = validator.isValid(
                    PluginUtils.getJarResourceAsStream(jar, DEFAULT_XML_DESCRIPTOR_FILE_NAME),
                    getClass().getResource(DEFAULT_XSD_PLUGIN_INFO_DESCRIPTOR_FILE_NAME));
                 if(isValid) {
                     pluginWrapper = super.loadPluginFromPath(pluginPath);
                     Plugin plugin = (Plugin) pluginWrapper.getPlugin();
                     plugin.setModulesDescriptor(createPluginModulesDescriptorFinder().findModules(pluginPath));
                 }
            } catch(PluginAlreadyLoadedException ex) {
                eventPublisher.publishEvent(new PluginAlreadyLoadedExceptionEvent(this, ex));
                return getPlugin(ex.getPluginId());
            } catch (IOException e) {
                throw new PluginException("The plugin descriptor is not valid");
            }
        }
        return pluginWrapper;
    }

    @Override
    protected boolean isDevelopment() {
        return Boolean.TRUE;
    }

    public PluginDescriptorValidator createPluginDescriptorValidator() {
        return new PluginDescriptorValidatorImpl();
    }
}


    