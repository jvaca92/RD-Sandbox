package my.sandbox.pf4j.spring.xml.descriptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;

public class PluginDescriptorValidatorImpl implements PluginDescriptorValidator {

    private static final Logger LOG = LoggerFactory.getLogger(PluginDescriptorValidatorImpl.class);

    @Override
    public boolean isValid(InputStream content, URL xsdURL) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(xsdURL);
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(content));
            return true;
        } catch (SAXException e) {
            LOG.error("The plugin descriptor in not valid. Reason: ", e);
            return false;
        } catch (IOException e) {
            LOG.error("Error during reading xml content:", e);
            return false;
        }
    }
}
