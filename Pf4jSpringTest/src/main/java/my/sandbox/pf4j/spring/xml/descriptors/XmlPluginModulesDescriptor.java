package my.sandbox.pf4j.spring.xml.descriptors;

import java.util.List;

public class XmlPluginModulesDescriptor implements PluginModulesDescriptor {

    private List<RestModuleDescriptor> restModuleDescriptor;

    private XmlPluginModulesDescriptor(Builder builder) {
        restModuleDescriptor = builder.restModuleDescriptor;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public List<RestModuleDescriptor> getRestModuleDescriptor() {
        return restModuleDescriptor;
    }


    public static final class Builder {
        private List<RestModuleDescriptor> restModuleDescriptor;

        private Builder() {
        }

        public Builder restModuleDescriptor(List<RestModuleDescriptor> val) {
            restModuleDescriptor = val;
            return this;
        }

        public XmlPluginModulesDescriptor build() {
            return new XmlPluginModulesDescriptor(this);
        }
    }
}
