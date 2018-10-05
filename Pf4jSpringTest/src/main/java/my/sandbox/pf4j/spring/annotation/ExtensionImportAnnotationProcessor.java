package my.sandbox.pf4j.spring.annotation;

import my.sandbox.pf4j.spring.ExtensionManager;
import my.sandbox.pf4j.spring.PluginManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ReflectionUtils;

import java.util.Arrays;
import java.util.List;


public class ExtensionImportAnnotationProcessor implements BeanPostProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(ExtensionImportAnnotationProcessor.class);

    private final ExtensionManager extensionManager;

    public ExtensionImportAnnotationProcessor() {
        this.extensionManager = ExtensionManager.getInstance();
    }

    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        LOG.debug("The bean instance is type of {}", o.getClass());

//        Arrays.stream(o.getClass().getDeclaredFields())
//                .forEach(field -> {
//                    LOG.debug("The type of field is {}", field.getType());
//                    if(field.getType().isAnnotationPresent(ExtensionImport.class)) {
//                        LOG.debug("The field with name {} is annotated with {}", field.getType(), ExtensionImport.class.getName());
//                        List extensions = extensionManager.getPluginManager().getExtensions(field.getClass());
//                        if(extensions != null && !extensions.isEmpty()) {
//                            LOG.debug("Was found extension with name {}", extensions.get(0).getClass().getName());
//                            Class<?> extensionClass = (Class<?>) extensions.get(0);
//                            Object extensionInstance = extensionManager.getExtension(extensionClass);
//                            field.setAccessible(Boolean.TRUE);
//                            ReflectionUtils.setField(field, o, extensionInstance);
//                            field.setAccessible(Boolean.FALSE);
//                            LOG.debug("Was set up extension instance with name {} for field {}", extensionInstance.getClass().getName(), field.getName());
//                        }
//                    }
//                });
        ReflectionUtils.doWithFields(o.getClass(), field -> {
            LOG.debug("The field class name is  {}", field.getType());
            //TODO - do reflection for case of instances as parameters in constructors
            if (field.isAnnotationPresent(ExtensionImport.class)) {
                LOG.debug("Annotation type {} is presented", field.getAnnotatedType().getType().getTypeName());
                field.setAccessible(true);
                List extensions = extensionManager.getPluginManager().getExtensions(field.getClass());
                if(extensions != null && !extensions.isEmpty()) {
                    LOG.debug("Was found extension with name {}", extensions.get(0).getClass().getName());
                    Class<?> extensionClass = (Class<?>) extensions.get(0);
                    Object extensionInstance = extensionManager.getExtension(extensionClass);
                    ReflectionUtils.setField(field, o, extensionInstance);
                    LOG.debug("Was set up extension instance with name {} for field {}", extensionInstance.getClass().getName(), field.getName());
                }
                field.setAccessible(false);
            }
        });
        return o;
    }

    @Override
    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
        return o;
    }
}
