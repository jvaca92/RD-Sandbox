package my.sandbox.pf4j.spring;

import my.sandbox.pf4j.spring.xml.descriptors.PluginModulesDescriptor;
import org.apache.commons.lang.StringUtils;
import org.omg.CORBA.Object;
import org.pf4j.Extension;
import org.pf4j.ExtensionPoint;
import org.pf4j.PluginException;
import org.pf4j.PluginWrapper;
import org.pf4j.spring.SpringPlugin;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class Plugin extends SpringPlugin {

    private Set<String> extensionClassNames;
    private PluginModulesDescriptor modulesDescriptor;

    public Plugin(PluginWrapper wrapper) {
        super(wrapper);
        extensionClassNames = new HashSet<>();
    }

    @Override
    public void stop() {
        ApplicationContext context = getApplicationContext();
        if ((context != null) && (context instanceof ConfigurableApplicationContext)) {
            ((ConfigurableApplicationContext) context).stop();
        }
    }

    @Override
    public void start() throws PluginException {
        ((ConfigurableApplicationContext) getApplicationContext()).start();
        super.start();
    }

    private void loadExtensionClassNames() throws IOException {
        AnnotationConfigApplicationContext applicationContext = (AnnotationConfigApplicationContext) getApplicationContext();
        applicationContext.getBeansWithAnnotation(Extension.class)
                .entrySet()
                .forEach(m -> extensionClassNames.add(m.getValue().getClass().getName()));
    }

    public Set<String> getPluginExtensionClassNames() throws IOException {
        if(extensionClassNames.isEmpty()) { loadExtensionClassNames(); }
        return extensionClassNames;
    }

    public PluginModulesDescriptor getModulesDescriptor() {
        return modulesDescriptor;
    }

    public void setModulesDescriptor(PluginModulesDescriptor modulesDescriptor) {
        this.modulesDescriptor = modulesDescriptor;
    }
}
