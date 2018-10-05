package my.sandbox.pf4j.spring.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;
import java.io.ByteArrayInputStream;
import java.io.StringReader;

public class XmlPluginDescriptorTransformer {

    public static <T> T transformXMLToObject(Class<?> clazz, StringReader inputStream) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
        Unmarshaller jaxbUnMarshaller = jaxbContext.createUnmarshaller();
        return (T) jaxbUnMarshaller.unmarshal(inputStream);
    }

    public static <T> T transformNodeToObject(Class<?> clazz, Node node) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
        Unmarshaller jaxbUnMarshaller = jaxbContext.createUnmarshaller();
        return (T) jaxbUnMarshaller.unmarshal(new DOMSource(node));
    }

    public static Node findNodeInDocument(String namespaceURI, String localName, String documentContent) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(Boolean.TRUE);
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(new ByteArrayInputStream(documentContent.getBytes()));
        return document.getElementsByTagNameNS(
                namespaceURI != null && !namespaceURI.isEmpty() ? namespaceURI : "*" ,
                localName != null && !localName.isEmpty() ? localName : "*").item(0);
    }
}
