package my.sandbox.fxml.sitemesh.filter;

import my.sandbox.fxml.sitemesh.tag.ImportTagRuleBundle;
import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;
import org.springframework.http.MediaType;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

public class FXMLSiteMeshFilter extends ConfigurableSiteMeshFilter {

    @Override
    protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
        builder.addDecoratorPath("/*", "/WEB-INF/template-main.vm")
                .addDecoratorPath("/fragment/", "/WEB-INF/test-fragment.vm")
                .addTagRuleBundle(new ImportTagRuleBundle())
                .setMimeTypes(MediaType.TEXT_XML.getType());


    }



    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        super.doFilter(servletRequest, servletResponse, filterChain);
    }
}
