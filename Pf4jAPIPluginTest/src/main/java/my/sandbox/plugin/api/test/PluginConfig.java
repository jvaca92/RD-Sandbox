package my.sandbox.plugin.api.test;

import my.sandbox.pf4j.spring.annotation.Plugin;
import org.pf4j.Extension;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Plugin
@Configuration
@ComponentScan(basePackages = "my.sandbox.plugin.api.test",
        includeFilters = { @ComponentScan.Filter(type = FilterType.ANNOTATION,  value = Extension.class) })
public class PluginConfig {
}
