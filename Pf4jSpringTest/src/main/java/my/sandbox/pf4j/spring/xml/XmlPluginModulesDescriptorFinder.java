package my.sandbox.pf4j.spring.xml;

import my.sandbox.pf4j.spring.utils.PluginUtils;
import my.sandbox.pf4j.spring.xml.descriptors.PluginModulesDescriptor;
import my.sandbox.pf4j.spring.xml.descriptors.RestModuleDescriptor;
import my.sandbox.pf4j.spring.xml.descriptors.XmlPluginModulesDescriptor;
import my.sandbox.pf4j.spring.xml.modules.FxPlugin;
import org.pf4j.PluginException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.file.Path;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

import static my.sandbox.pf4j.spring.xml.PluginDescriptorProperties.DEFAULT_XML_DESCRIPTOR_FILE_NAME;

public class XmlPluginModulesDescriptorFinder implements PluginModulesDescriptorFinder {

    private static final Logger LOG = LoggerFactory.getLogger(XmlPluginModulesDescriptorFinder.class);

    @Override
    public PluginModulesDescriptor findModules(Path path) throws PluginException {

        try (JarFile jar = new JarFile(path.toFile())) {
            BufferedReader buffer = new BufferedReader(new InputStreamReader(PluginUtils.getJarResourceAsStream(jar, DEFAULT_XML_DESCRIPTOR_FILE_NAME)));
            String content = buffer.lines().collect(Collectors.joining(System.lineSeparator()));
            if(content == null) {
                throw new PluginException("The plugin descriptor with modules was not found");
            }
            XmlPluginModulesDescriptor.Builder builder = XmlPluginModulesDescriptor.newBuilder();
            FxPlugin plugin = XmlPluginDescriptorTransformer.transformXMLToObject(FxPlugin.class, new StringReader(content));

            builder.restModuleDescriptor(plugin.getRestModule()
                    .stream()
                    .map(restModule -> RestModuleDescriptor.newBuilder()
                            .key(restModule.getKey())
                            .description(restModule.getDescription())
                            .version(restModule.getVersion())
                            .clazz(restModule.getClazz())
                            .build())
                    .collect(Collectors.toList()));

            return builder.build();
        } catch (Exception e) {
            LOG.error("Error during creating plugin descriptor: ", e);
            throw new PluginException(e);
        }
    }

}
