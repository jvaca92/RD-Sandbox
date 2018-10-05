package my.sandbox.pf4j.spring.xml.descriptors;

import java.io.InputStream;
import java.net.URL;

public interface PluginDescriptorValidator {

    /**
     * Validate plugin descriptor content base on xsd
     * @param content -
     * @param xsdURL
     * @return
     */
    boolean isValid(InputStream content, URL xsdURL);
}
