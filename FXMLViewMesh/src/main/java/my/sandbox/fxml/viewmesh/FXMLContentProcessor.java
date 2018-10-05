package my.sandbox.fxml.viewmesh;

import my.sandbox.fxml.viewmesh.utils.StreamUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import javax.xml.stream.*;
import javax.xml.stream.events.XMLEvent;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class FXMLContentProcessor {

    private static final Logger LOG = Logger.getLogger(FXMLContentProcessor.class);
    private Set<String> IMPORTS = new HashSet<>();
    private Map<String, String> DECORATORS = new HashMap<>();

    /** Command type which will wrap part of text in the defined decorator **/
    private static final String FXML_COMMAND_PREFIX = "fx";
    private static final String WRAP_COMMAND_TYPE = "fx:wrap";
    private static final String WRITE_COMMAND_TYPE = "fx:write";

    private final XMLInputFactory inputFactory;
    private final XMLOutputFactory outputFactory;
    private final XMLEventFactory eventFactory;

    public FXMLContentProcessor() throws IOException {
        this.inputFactory = XMLInputFactory.newInstance();
        this.outputFactory = XMLOutputFactory.newInstance();
        this.eventFactory = XMLEventFactory.newInstance();

        //Fill test data
        DECORATORS.putAll(new HashMap<String, String>(){{
            put("template1", StreamUtils.readInputStreamAsString(getClass().getResourceAsStream("/decorators/template1.vm"), StandardCharsets.UTF_8));
            put("template2", StreamUtils.readInputStreamAsString(getClass().getResourceAsStream("/decorators/template2.vm"), StandardCharsets.UTF_8));
            put("template3", StreamUtils.readInputStreamAsString(getClass().getResourceAsStream("/decorators/template3.vm"), StandardCharsets.UTF_8));
        }});
    }


    public String processContent(String content, String content2) throws XMLStreamException, UnsupportedEncodingException {

        ByteArrayOutputStream result = new ByteArrayOutputStream();
        XMLStreamReader reader = inputFactory.createXMLStreamReader(new StringReader(content));
        XMLStreamWriter writer = outputFactory.createXMLStreamWriter(result);
        Boolean isDecorated = Boolean.FALSE;

        while(reader.hasNext()) {
            switch (reader.getEventType()) {

                /*<?xml version="1.0" encoding="UTF-8"?>*/
                case XMLStreamConstants.START_DOCUMENT:
                    reader.next();
                    break;
                /*  Java imports */
                case XMLStreamConstants.PROCESSING_INSTRUCTION:
                    IMPORTS.add(MessageFormat.format("<?{0} {1}?>", reader.getPITarget(), reader.getPIData()));
                    reader.next();
                    break;
                case XMLStreamConstants.START_ELEMENT:
                     if(reader.getPrefix().equals(FXML_COMMAND_PREFIX)) {
                        switch (reader.getLocalName()) {
                            case "decorate":
                                String attrVal = reader.getAttributeValue(null, "name");
                                if(attrVal != null && !attrVal.isEmpty() && DECORATORS.containsKey(attrVal)) {
                                    processContent(DECORATORS.get(attrVal), new String(result.toByteArray(), StandardCharsets.UTF_8));
                                }
                                break;
                            case "write":
                                if(content2 != null && !content2.isEmpty()) {
                                    writer.writeCharacters(content2);
                                }
                                break;
                            default:
                                processStartElement(writer, reader);
                                break;
                        }
                     }
                     reader.next();
                     break;
                case XMLStreamConstants.END_ELEMENT:
                    writer.writeEndElement();
                    reader.next();
                    break;
                case XMLStreamConstants.CHARACTERS:
                    writer.writeCharacters(reader.getText());
                    reader.next();
                    break;

            }
        }
        writer.flush();
        writer.close();

        return  new String(result.toByteArray(), StandardCharsets.UTF_8);
    }

//    private void processParentContent(String parentContent, OutputStream outputStream) throws XMLStreamException {
//        ByteArrayOutputStream parentRe = new ByteArrayOutputStream();
//        XMLStreamReader reader = inputFactory.createXMLStreamReader(new StringReader(parentContent));
//        XMLStreamWriter writer = outputFactory.createXMLStreamWriter();
//        while(reader.hasNext()) {
//            switch (reader.getEventType()) {
//                /*<?xml version="1.0" encoding="UTF-8"?>*/
//                case XMLStreamConstants.START_DOCUMENT:
//                    break;
//                /*  Java imports */
//                case XMLStreamConstants.PROCESSING_INSTRUCTION:
//                    IMPORTS.add(MessageFormat.format("<?{0} {1}?>", reader.getPITarget(), reader.getPIData()));
//                    break;
//                case XMLStreamConstants.START_ELEMENT:
//                    if (reader.getPrefix().equals(FXML_COMMAND_PREFIX)) {
//                        switch (reader.getLocalName()) {
//                            case "decorate":
//                                String attrVal = reader.getAttributeValue(null, "name");
//                                if (attrVal != null && !attrVal.isEmpty()) {
//                                    //TODO handle if the docorator is not found
//                                    processParentContent(DECORATORS.get(attrVal), outputStream);
//                                }
//                                break;
//                            case "write":
//                                String propertyValue = reader.getAttributeValue(null, "property");
//                                switch (propertyValue) {
//                                    case "content":
//                                    writer.flush();
//
//                                }
//                                break;
//                            default:
//                                processStartElement(writer, reader);
//                                break;
//                        }
//                    }
//                    break;
//                case XMLStreamConstants.END_ELEMENT:
//                    writer.writeEndElement();
//                    break;
//            }
//        }
//    }

    private void processStartElement(XMLStreamWriter streamWriter, XMLStreamReader streamReader) throws XMLStreamException {
                /* First create start tag */
            if(streamReader.getPrefix() != null && !streamReader.getPrefix().isEmpty()) {
                streamWriter.writeStartElement(streamReader.getPrefix(), streamReader.getLocalName(), null);
            } else {
                streamWriter.writeStartElement(streamReader.getLocalName());
            }
            if(streamReader.getAttributeCount() != 0) {
                IntStream.range(0, streamReader.getAttributeCount()).forEach(
                        value -> {
                            QName attribute = streamReader.getAttributeName(value);
                            try {
                                streamWriter.writeAttribute(attribute.getPrefix(),
                                        attribute.getNamespaceURI(),
                                        attribute.getLocalPart(),
                                        streamReader.getAttributeValue(value));
                            } catch (XMLStreamException e) {
                                LOG.error("Error creating start element: {}", e);
                            }
                        }
                );
            } else {
                LOG.info("Element was not contain any attributes. Not any attributes will be written");
            }
    }


    private String getAttributeValue(XMLStreamReader streamReader, String prefix, String localPart) {
        QName attribute = null;
        for(int i = 0; i < streamReader.getAttributeCount(); i++) {

            attribute = streamReader.getAttributeName(i);
            if(attribute.getPrefix() != null && !attribute.getPrefix().isEmpty()) {
                if(attribute.getLocalPart().equals(localPart) && attribute.getPrefix().equals(prefix)) {
                    return streamReader.getAttributeValue(i);
                }
            } else if(attribute.getLocalPart().equals(localPart)) {
                return streamReader.getAttributeValue(i);
            }
        }
        return "";
    }

    public String getDecorator(String name) {
        return DECORATORS.get(name);
    }

    public Map<String, String> getDecorators() {
        return DECORATORS;
    }
}
