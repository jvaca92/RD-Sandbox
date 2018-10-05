package my.sandbox.pf4j.plugins.test;


import my.sandbox.pf4j.plugins.test.internal.MessageProvider;
import my.sandbox.pf4j.plugins.test.internal.MessageProviderImpl;
import my.sandbox.pf4j.spring.annotation.Plugin;
import org.glassfish.jersey.servlet.ServletContainer;
import org.pf4j.Extension;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Plugin
@Configuration
@ComponentScan(basePackages = "my.sandbox.pf4j.plugins.test",
        includeFilters = { @ComponentScan.Filter(type = FilterType.ANNOTATION,  value = Extension.class) })
public class PluginConfig {
}
