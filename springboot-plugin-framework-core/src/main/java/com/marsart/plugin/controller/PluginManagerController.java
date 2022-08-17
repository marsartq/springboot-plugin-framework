package com.marsart.plugin.controller;

import com.marsart.plugin.PluginManagerService;
import com.marsart.plugin.entity.PluginInfo;
import com.marsart.plugin.entity.PluginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Paths;
import java.util.List;


/**
 * @author qiyao(1210)
 * 2022-04-11
 */
@RestController
@RequestMapping("/hlms/api/plugin/manage")
public class PluginManagerController {
    @Autowired
    private PluginManagerService service;

    @PostMapping(value = "/list", consumes = {"application/json"})
    public List<PluginInfo> getStartPlugins() {
        return service.getInstallSpringPlugins();
    }

    @PostMapping(path = "/stop", consumes = {"application/json"})
    public boolean unloadPlugin(@RequestBody PluginRequest request) {
        if (request.getPluginId() == null || request.getPluginId().isEmpty()) {
            return false;
        }
        try {
            service.stopSpringPlugin(request.getPluginId(), request.isUnload());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @PostMapping(path = "/start", consumes = {"application/json"})
    public boolean startPlugin(@RequestBody PluginRequest request) {
        if (request.getPluginId() == null || request.getPluginId().isEmpty()) {
            return false;
        }
        try {
            return service.startSpringPlugin(request.getPluginId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @PostMapping(path = "/install", consumes = {"application/json"})
    public String installPlugin(@RequestBody PluginRequest request) {
        if (request.getPluginPath() == null || request.getPluginPath().isEmpty()) {
            return "";
        }
        try {
            return service.installSpringPlugin(Paths.get(request.getPluginPath()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
