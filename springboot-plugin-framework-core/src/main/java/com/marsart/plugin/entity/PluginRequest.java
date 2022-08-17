package com.marsart.plugin.entity;

/**
 * @author qiyao(1210)
 * @date 2022-04-12
 */
public class PluginRequest {
    private String pluginId;
    private String pluginPath;
    private boolean unload;

    public String getPluginId() {
        return pluginId;
    }

    public void setPluginId(String pluginId) {
        this.pluginId = pluginId;
    }

    public String getPluginPath() {
        return pluginPath;
    }

    public void setPluginPath(String pluginPath) {
        this.pluginPath = pluginPath;
    }

    public boolean isUnload() {
        return unload;
    }

    public void setUnload(boolean unload) {
        this.unload = unload;
    }
}
