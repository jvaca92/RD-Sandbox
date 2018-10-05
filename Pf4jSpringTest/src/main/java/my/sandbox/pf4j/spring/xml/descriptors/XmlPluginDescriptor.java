package my.sandbox.pf4j.spring.xml.descriptors;

import org.pf4j.Plugin;
import org.pf4j.PluginDependency;
import org.pf4j.PluginDescriptor;

import java.util.ArrayList;
import java.util.List;

public class XmlPluginDescriptor implements PluginDescriptor {

    private String pluginId;
    private String pluginDescription;
    private String pluginClass = Plugin.class.getName();
    private String version;
    private String requires = "*"; // SemVer format
    private String provider;
    private List<PluginDependency> dependencies;
    private String license;

    public XmlPluginDescriptor() {
        dependencies = new ArrayList();
    }

    private XmlPluginDescriptor(Builder builder) {
        pluginId = builder.pluginId;
        pluginDescription = builder.pluginDescription;
        pluginClass = builder.pluginClass;
        version = builder.version;
        requires = builder.requires;
        provider = builder.provider;
        dependencies = builder.dependencies;
        license = builder.license;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public String getPluginId() {
        return pluginId;
    }

    @Override
    public String getPluginDescription() {
        return pluginDescription;
    }

    @Override
    public String getPluginClass() {
        return pluginClass;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public String getRequires() {
        return requires;
    }

    @Override
    public String getProvider() {
        return provider;
    }

    @Override
    public List<PluginDependency> getDependencies() {
        return dependencies;
    }

    @Override
    public String getLicense() {
        return license;
    }

    public static final class Builder {
        private String pluginId;
        private String pluginDescription;
        private String pluginClass = Plugin.class.getName();
        private String version;
        private String requires = "*"; // SemVer format
        private String provider;
        private List<PluginDependency> dependencies;
        private String license;


        private Builder() {
        }

        public Builder pluginId(String val) {
            pluginId = val;
            return this;
        }

        public Builder pluginDescription(String val) {
            pluginDescription = val;
            return this;
        }

        public Builder pluginClass(String val) {
            pluginClass = val;
            return this;
        }

        public Builder version(String val) {
            version = val;
            return this;
        }

        public Builder requires(String val) {
            requires = val;
            return this;
        }

        public Builder provider(String val) {
            provider = val;
            return this;
        }

        public Builder dependencies(String dependencies) {
            if (dependencies != null) {
                dependencies = dependencies.trim();
                if (dependencies.isEmpty()) {
                    this.dependencies = new ArrayList<>();
                } else {
                    this.dependencies = new ArrayList<>();
                    String[] tokens = dependencies.split(",");
                    for (String dependency : tokens) {
                        dependency = dependency.trim();
                        if (!dependency.isEmpty()) {
                            this.dependencies.add(new PluginDependency(dependency));
                        }
                    }
                    if (this.dependencies.isEmpty()) {
                        this.dependencies = new ArrayList<>();
                    }
                }
            } else {
                this.dependencies = new ArrayList<>();
            }

            return this;
        }

        public Builder license(String val) {
            license = val;
            return this;
        }

        public XmlPluginDescriptor build() {
            return new XmlPluginDescriptor(this);
        }
    }


    @Override
    public String toString() {
        return "PluginDescriptor [pluginId=" + pluginId + ", pluginClass="
                + pluginClass + ", version=" + version + ", provider="
                + provider + ", dependencies=" + dependencies + ", description="
                + pluginDescription + ", requires=" + requires + ", license="
                + license + "]";
    }
}
