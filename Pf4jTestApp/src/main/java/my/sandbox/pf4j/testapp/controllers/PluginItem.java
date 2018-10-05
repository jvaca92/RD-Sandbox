package my.sandbox.pf4j.testapp.controllers;

import javafx.beans.property.SimpleStringProperty;

public class PluginItem {

    private SimpleStringProperty pluginID;
    private SimpleStringProperty pluginDesc;

    private PluginItem(Builder builder) {
        pluginID = builder.pluginID;
        pluginDesc = builder.pluginDesc;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public String getPluginID() {
        return pluginID.get();
    }

    public SimpleStringProperty pluginIDProperty() {
        return pluginID;
    }

    public void setPluginID(String pluginID) {
        this.pluginID.set(pluginID);
    }

    public String getPluginDesc() {
        return pluginDesc.get();
    }

    public SimpleStringProperty pluginDescProperty() {
        return pluginDesc;
    }

    public void setPluginDesc(String pluginDesc) {
        this.pluginDesc.set(pluginDesc);
    }


    public static final class Builder {
        private SimpleStringProperty pluginID;
        private SimpleStringProperty pluginDesc;

        private Builder() {
        }

        public Builder pluginID(SimpleStringProperty val) {
            pluginID = val;
            return this;
        }

        public Builder pluginDesc(SimpleStringProperty val) {
            pluginDesc = val;
            return this;
        }

        public PluginItem build() {
            return new PluginItem(this);
        }
    }
}
