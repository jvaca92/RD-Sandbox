package my.sandbox.pf4j.spring;

import my.sandbox.pf4j.spring.annotation.ExtensionImportAnnotationProcessor;
import org.pf4j.PluginException;
import org.pf4j.PluginWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class PluginFactory implements org.pf4j.PluginFactory {

    private static final Logger LOG = LoggerFactory.getLogger(PluginFactory.class);

    @Override
    public org.pf4j.Plugin create(PluginWrapper pluginWrapper) {

        String pluginClazzName = pluginWrapper.getDescriptor().getPluginClass();
        LOG.debug("The plugin class name is {}", pluginClazzName);

        /** Plugin class name has to be annotated as configuration and as plugin ***/
        Class<?> pluginClass;
        try {
            pluginClass = pluginWrapper.getPluginClassLoader().loadClass(pluginClazzName);
            //Let validate if contains annotation plugin
            LOG.debug("The plugin class has {} annotations", pluginClass.getAnnotations().length);
            if(!pluginClass.isAnnotationPresent(my.sandbox.pf4j.spring.annotation.Plugin.class)) {
                LOG.error("The plugin does not contain plugin annotation...");
                return null;
            }
            return createPluginInstance(pluginWrapper, pluginClass);
        } catch (ClassNotFoundException e) {
            LOG.error("The plugin class or config not found: {}", e);
            return null;
        }
    }

    /** Will create new plugin instance ***/
    private org.pf4j.Plugin createPluginInstance(PluginWrapper pluginWrapper, Class pluginConfigClass) {
        return new Plugin(pluginWrapper) {

            @Override
            public void start() throws PluginException {
                LOG.debug("Starting plugin with id {}", pluginWrapper.getPluginId());
                super.start();
            }

            @Override
            public void stop() {
                LOG.debug("Stopping plugin with id {}", pluginWrapper.getPluginId());
                super.stop();
            }

            @Override
            protected ApplicationContext createApplicationContext() {
                AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
                BeanPostProcessor beanPostProcessor = new ExtensionImportAnnotationProcessor();
                BeanFactoryPostProcessor beanFactoryPostProcessor = createBeanFactoryPostProcessor(beanPostProcessor);
                applicationContext.addBeanFactoryPostProcessor(beanFactoryPostProcessor);
                applicationContext.setClassLoader(getWrapper().getPluginClassLoader());
                applicationContext.register(pluginConfigClass);
                applicationContext.refresh();
                return applicationContext;
            }

            protected BeanFactoryPostProcessor createBeanFactoryPostProcessor(BeanPostProcessor beanPostProcessor) {
                return new BeanFactoryPostProcessor() {
                    @Override
                    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
                        configurableListableBeanFactory.addBeanPostProcessor(beanPostProcessor);
                    }
                };
            }
//            private Set<BeanDefinition> findExtensionAnnotatedClasses(PluginWrapper wrapper) {
//                ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(Boolean.FALSE);
//                provider.addIncludeFilter(new AnnotationTypeFilter(Extension.class));
//                provider.setResourceLoader(new PathMatchingResourcePatternResolver(wrapper.getPluginClassLoader()));
//                return provider.findCandidateComponents("my.sandbox.pf4j.plugins.test");    //Just test base package
//            }
        };
    }


}
