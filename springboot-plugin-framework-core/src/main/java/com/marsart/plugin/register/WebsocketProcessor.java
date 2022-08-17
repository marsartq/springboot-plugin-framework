package com.marsart.plugin.register;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.marsart.plugin.NtsPlugin;
import com.marsart.plugin.PluginPipeProcessor;


/**
 * @author qiyao(1210)
 * @date 2022-03-25
 */
public class WebsocketProcessor implements PluginPipeProcessor {
    @Override
    public void initialize() {
    }

    @Override
    public void registry(NtsPlugin ntsPlugin) throws Exception {
        ApplicationContext pluginContext = ntsPlugin.getPluginApplicationContext();
        ApplicationContext mainApplicationContext = ntsPlugin.getMainApplicationContext();
        if (pluginContext.containsBean("webSocketHandlerMapping")
          && !mainApplicationContext.containsBean("webSocketHandlerMapping")) {
            ((GenericWebApplicationContext) mainApplicationContext).getDefaultListableBeanFactory()
                .registerSingleton("webSocketHandlerMapping", pluginContext.getBean("webSocketHandlerMapping"));
            DispatcherServlet dispatcherServlet = (DispatcherServlet) mainApplicationContext.getBean("dispatcherServlet");
        }
    }

    @Override
    public void unRegistry(NtsPlugin ntsPlugin) throws Exception {
        // WebSocketHandlerMapping webSocketHandlerMapping = ntsPlugin.;
        // webSocketHandlerMapping.getHandler();
        // TODO: 暂时无法注销
    }
}
