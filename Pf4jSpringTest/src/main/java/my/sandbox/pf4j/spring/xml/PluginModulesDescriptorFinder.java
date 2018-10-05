package my.sandbox.pf4j.spring.xml;

import my.sandbox.pf4j.spring.xml.descriptors.PluginModulesDescriptor;
import org.pf4j.PluginException;

import java.nio.file.Path;

public interface PluginModulesDescriptorFinder {

    PluginModulesDescriptor findModules(Path path) throws PluginException;
}
