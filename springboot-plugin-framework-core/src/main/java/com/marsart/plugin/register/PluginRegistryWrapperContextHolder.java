package com.marsart.plugin.register;

import com.marsart.plugin.NtsPlugin;
import com.marsart.plugin.entity.PluginInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qiyao(1210)
 * @date 2022-03-09
 */
public class PluginRegistryWrapperContextHolder {

    private static final Map<String, NtsPlugin> pluginRegistryMaps = Collections.synchronizedMap(new HashMap<>());
    private static final Map<String, PluginInfo> pluginInfoMaps = Collections.synchronizedMap(new HashMap<>());

    public static boolean put(String pluginId, NtsPlugin ntsPlugin) {
        if (pluginRegistryMaps.containsKey(pluginId)) {
            return false;
        }
        pluginRegistryMaps.put(pluginId, ntsPlugin);
        pluginInfoMaps.put(pluginId, new PluginInfo(ntsPlugin.getPluginWrapper()));
        return true;
    }

    public static NtsPlugin getPluginRegistryWrapper(String pluginId) {
        return pluginRegistryMaps.get(pluginId);
    }

    public static void remove(String pluginId, boolean unload) {
        if (pluginInfoMaps.containsKey(pluginId)) {
            if (unload) {
                pluginInfoMaps.get(pluginId).setPluginStatus("UNLOAD");
            } else {
                pluginInfoMaps.get(pluginId).setPluginStatus("STOP");
            }
        }
        pluginRegistryMaps.remove(pluginId);
    }

    public static List<PluginInfo> getPluginStatusList() {
        return new ArrayList<>(pluginInfoMaps.values());
    }

    public static PluginInfo getPluginInfo(String pluginId) {
        return pluginInfoMaps.get(pluginId);
    }
}
