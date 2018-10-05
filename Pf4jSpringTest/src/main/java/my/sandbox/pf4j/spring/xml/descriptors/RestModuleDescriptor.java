package my.sandbox.pf4j.spring.xml.descriptors;

import java.util.List;

public class RestModuleDescriptor {

    protected String description;
    protected List<String> clazz;
    protected String key;
    protected Float version;

    private RestModuleDescriptor(Builder builder) {
        description = builder.description;
        clazz = builder.clazz;
        key = builder.key;
        version = builder.version;
    }

    public static Builder newBuilder() {
        return new Builder();
    }


    public String getDescription() {
        return description;
    }

    public List<String> getClazz() {
        return clazz;
    }

    public String getKey() {
        return key;
    }

    public Float getVersion() {
        return version;
    }

    public static final class Builder {
        private String description;
        private List<String> clazz;
        private String key;
        private Float version;

        private Builder() {
        }

        public Builder description(String val) {
            description = val;
            return this;
        }

        public Builder clazz(List<String> val) {
            clazz = val;
            return this;
        }

        public Builder key(String val) {
            key = val;
            return this;
        }

        public Builder version(Float val) {
            version = val;
            return this;
        }

        public RestModuleDescriptor build() {
            return new RestModuleDescriptor(this);
        }
    }
}
