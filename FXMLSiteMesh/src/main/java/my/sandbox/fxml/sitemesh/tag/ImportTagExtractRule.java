package my.sandbox.fxml.sitemesh.tag;

import org.apache.log4j.Logger;
import org.sitemesh.content.ContentProperty;
import org.sitemesh.tagprocessor.BasicRule;
import org.sitemesh.tagprocessor.Tag;


import java.io.IOException;
import java.text.MessageFormat;

public class ImportTagExtractRule extends BasicRule {

    private final Logger LOG = org.apache.log4j.Logger.getLogger(ImportTagExtractRule.class);

    private final ContentProperty contentProperty;

    public ImportTagExtractRule(ContentProperty contentProperty) {
        this.contentProperty = contentProperty;
    }

    @Override
    public void process(Tag tag) throws IOException {
        LOG.debug(MessageFormat.format("Content property value {0}", contentProperty.getValue()));

    }
}
