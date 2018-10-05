package my.sandbox.pf4j.spring;

import org.pf4j.*;
import org.pf4j.PluginManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.io.IOException;
import java.util.*;

public class ServiceExtensionFinder extends AbstractExtensionFinder implements PluginStateListener {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceExtensionFinder.class);

    public ServiceExtensionFinder(PluginManager pluginManager) {
        super(pluginManager);
    }

    @Override
    public Map<String, Set<String>> readPluginsStorages() {
        Map<String, Set<String>> pluginResults = new LinkedHashMap<>();
        pluginManager.getPlugins().forEach(
                pluginWrapper -> {
                    LOG.debug("Start read plugin storage of plugin {}", pluginWrapper.getPluginId());
                    if(pluginWrapper.getPlugin() instanceof Plugin) {
                        Plugin plugin = (Plugin) pluginWrapper.getPlugin();
                        try {
                            pluginResults.put(pluginWrapper.getPluginId(), plugin.getPluginExtensionClassNames());
                        } catch (IOException e) {
                            LOG.error("Error during loading extension class names: {}", e.getMessage());
                        }
                    }
                }
        );
        return pluginResults;
    }

    @Override
    public Map<String, Set<String>> readClasspathStorages() {
        Map<String, Set<String>> classpathResults = new LinkedHashMap<>();
        Set<String> entries = new HashSet<>();
        //Let find all export services in classpath
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(Boolean.FALSE);
        scanner.addIncludeFilter(new AnnotationTypeFilter(Extension.class));
        scanner.findCandidateComponents("my.sandbox.*")   //Just for test has to be changed
                .forEach(beanDefinition -> {
                    LOG.debug("Was found extension {} in classpath", beanDefinition.getBeanClassName());
                    try {
                        Class beanClazz = Class.forName(beanDefinition.getBeanClassName());
                        if(beanClazz.isAnnotationPresent(Extension.class));
                        {
                            entries.add(beanDefinition.getBeanClassName());
                            LOG.debug("The extension {} was added into the entries", beanDefinition.getBeanClassName());
                        }
                    } catch (ClassNotFoundException e) {
                       LOG.error("The class was not found {}", e);
                    }
                });
        classpathResults.put(null, entries);
        return classpathResults;
    }

    @Override
    public void pluginStateChanged(PluginStateEvent pluginStateEvent) {
        
    }
}
