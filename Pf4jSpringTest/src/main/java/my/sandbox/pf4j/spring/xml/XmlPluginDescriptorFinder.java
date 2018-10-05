package my.sandbox.pf4j.spring.xml;

import my.sandbox.pf4j.spring.utils.PluginUtils;
import my.sandbox.pf4j.spring.xml.descriptors.XmlPluginDescriptor;
import my.sandbox.pf4j.spring.xml.modules.FxPlugin;
import my.sandbox.pf4j.spring.xml.modules.PluginInfo;
import org.pf4j.DefaultPluginDescriptor;
import org.pf4j.PluginDescriptor;
import org.pf4j.PluginDescriptorFinder;
import org.pf4j.PluginException;
import org.pf4j.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

import static my.sandbox.pf4j.spring.xml.PluginDescriptorProperties.DEFAULT_XML_DESCRIPTOR_FILE_NAME;

/**
 * This class will be loading plugin xml descriptor
 */
public class XmlPluginDescriptorFinder implements PluginDescriptorFinder {

    private static final Logger LOG = LoggerFactory.getLogger(XmlPluginDescriptorFinder.class);

    protected String xmlDescriptorFileName;

    public XmlPluginDescriptorFinder() {
        this(DEFAULT_XML_DESCRIPTOR_FILE_NAME);
    }

    public XmlPluginDescriptorFinder(String xmlDescriptorFileName) {
            this.xmlDescriptorFileName = xmlDescriptorFileName;
    }

    @Override
    public boolean isApplicable(Path path) {
        return Files.exists(path) && (Files.isDirectory(path) || FileUtils.isJarFile(path));
    }

    @Override
    public PluginDescriptor find(Path path) throws PluginException {

            try (JarFile jar = new JarFile(path.toFile())) {
                BufferedReader buffer = new BufferedReader(new InputStreamReader(PluginUtils.getJarResourceAsStream(jar, DEFAULT_XML_DESCRIPTOR_FILE_NAME)));
                String content = buffer.lines().collect(Collectors.joining(System.lineSeparator()));
                if(content == null) {
                   throw new PluginException("The plugin descriptor was not found");
                }
                if(LOG.isDebugEnabled()) {
                    Arrays.stream(FxPlugin.class.getDeclaredFields()).forEach(field -> {
                        LOG.debug("Plugin descriptor class contains variable with name {}", field.getType());
                        Arrays.stream(field.getDeclaredAnnotations())
                                .forEach(annotation -> LOG.debug("The plugin variable is annotated by annotation {}",
                                        annotation.annotationType().getName()));
                    });
                }
                String pluginInfoElemName = Arrays.stream(FxPlugin.class.getDeclaredFields())
                        .filter(field -> field.getType().getName().equals(PluginInfo.class.getName())
                                && field.isAnnotationPresent(XmlElement.class))
                        .findFirst()
                        .get()
                        .getAnnotation(XmlElement.class)
                        .name();
                Node pluginInfoNode = XmlPluginDescriptorTransformer.findNodeInDocument(null, pluginInfoElemName, content);
                PluginInfo pluginInfo = XmlPluginDescriptorTransformer.transformNodeToObject(PluginInfo.class, pluginInfoNode);
                return createPluginDescriptor(pluginInfo);
            } catch (Exception e) {
                LOG.error("Error during creating plugin descriptor: ", e);
                throw new PluginException(e);
            }
    }


    protected PluginDescriptor createPluginDescriptor(PluginInfo pluginInfo) {
        return XmlPluginDescriptor.newBuilder()
                .pluginId(pluginInfo.getId())
                .pluginClass(pluginInfo.getClazz())
                .pluginDescription(pluginInfo.getDescription())
                .provider(pluginInfo.getProvider())
                .dependencies(pluginInfo.getDependencies())
                .version(pluginInfo.getVersion())
                .build();
    }

    protected DefaultPluginDescriptor createPluginDescriptorInstance() {
        return new DefaultPluginDescriptor();
    }

}
