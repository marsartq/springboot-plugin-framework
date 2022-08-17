package com.marsart.plugin;

import java.nio.file.Paths;

import com.marsart.plugin.register.ClassProcessor;
import com.marsart.plugin.register.ResourceLoaderProcessor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


/**
 * @author qiyao(1210)
 * @date 2022-04-24
 */
public class Boot {

    public static void main(String[] args) {
        SpringPluginManager springPluginManager = new SpringPluginManager(Paths.get("plugins"));
        springPluginManager.loadPlugins();
        System.out.println("load end");
        springPluginManager.startPlugins();
        System.out.println("start end");
        AnnotationConfigApplicationContext mainContext = new AnnotationConfigApplicationContext();
        NtsPlugin ntsPlugin = new NtsPlugin(springPluginManager.getPlugin("test-plugin"), mainContext);

        try {
            ResourceLoaderProcessor resourceLoaderProcessor = new ResourceLoaderProcessor();
            resourceLoaderProcessor.initialize();
            resourceLoaderProcessor.registry(ntsPlugin);

            ClassProcessor classProcessor = new ClassProcessor();
            classProcessor.initialize();
            classProcessor.registry(ntsPlugin);
            System.out.println("class registry end");
            classProcessor.unRegistry(ntsPlugin);
            resourceLoaderProcessor.unRegistry(ntsPlugin);
            ntsPlugin.unload();
            ntsPlugin = null;
            springPluginManager.stopPlugin("test-plugin");
            System.out.println("stop end");
            springPluginManager.unloadPlugin("test-plugin");
            System.out.println("unload end");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
