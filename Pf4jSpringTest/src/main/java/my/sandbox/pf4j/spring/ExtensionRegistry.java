package my.sandbox.pf4j.spring;
import org.picocontainer.MutablePicoContainer;
import java.util.*;
import java.util.stream.Collectors;

public class ExtensionRegistry {

    private final MutablePicoContainer extensionContainer;

    public ExtensionRegistry(MutablePicoContainer extensionContainer) {
        this.extensionContainer = extensionContainer;
    }

    public void registerExtension(Class<?> clazz, Object instance) {
        extensionContainer.addComponent(clazz, instance);
    }

    public void unregisterExtension(Class<?> clazz) {
        extensionContainer.removeComponentByInstance(getRegisteredExtension(clazz));
    }

    public <T> T getRegisteredExtension(Class<?> clazz) {
        return (T) extensionContainer.getComponent(clazz);
    }

    public void updateExtension(Class<?> clazz, Object instance) {
        if(extensionContainer.getComponents().contains(instance)) {
            extensionContainer.addComponent(clazz, instance);
        } else if(!extensionContainer.getComponent(clazz).equals(instance)) {
            extensionContainer.removeComponent(extensionContainer.getComponent(clazz));
            extensionContainer.addComponent(clazz, instance);
        }
    }

    public boolean containsExtensionOfType(Class<?> clazz) {
        return !extensionContainer.getComponents()
                .stream()
                .filter(extension -> extension.getClass().equals(clazz))
                .collect(Collectors.toList())
                .isEmpty();
    }

    public boolean containsExtensionInstance(Object instance) {
        return extensionContainer.getComponents().contains(instance);
    }

    public List getAllExtensionInstances() {
        return extensionContainer.getComponents();
    }

}
