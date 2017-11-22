package com.gam.nocr.ems.data.domain.vol;

/**
 * Created by Najafi Sahar najafisahaar@yahoo.com on 11/15/17.
 */
public class PluginInfoVTO {
    private String id;
    private String pluginName;
    private String state;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPluginName() {
        return pluginName;
    }

    public void setPluginName(String pluginName) {
        this.pluginName = pluginName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
