package com.marsart.plugin.register;

import java.util.ArrayList;
import java.util.List;

import com.marsart.plugin.NtsPlugin;
import com.marsart.plugin.PluginPipeProcessor;
import com.marsart.plugin.register.classgroup.*;
import org.pf4j.Plugin;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;


/**
 * 扫描所有的class 并按照指定的class类分类
 *
 * @author qiyao(1210)
 * @date 2022-03-09
 */
public class ClassProcessor implements PluginPipeProcessor {

    List<PluginClassGroup> groups = new ArrayList<>();

    @Override
    public void initialize() {

    }

    @Override
    public void registry(NtsPlugin ntsPlugin) throws Exception {
        List<Class<?>> classList = new ArrayList<>();
        for (Resource resource : ntsPlugin.getClassResourceList()) {
            if (resource.isReadable()) {
                MetadataReader metadataReader = new CachingMetadataReaderFactory().getMetadataReader(resource);
                Class clazz = ntsPlugin.getPluginWrapper().getPluginClassLoader()
                    .loadClass(metadataReader.getAnnotationMetadata().getClassName());
                if (!Plugin.class.isAssignableFrom(clazz)) {
                    classList.add(clazz);
                    groups.forEach(group -> {
                        if (group.filter(clazz)) {
                            ntsPlugin.addClassToGroup(group.groupId(), clazz);
                        }
                    });
                }
            }
        }
        ntsPlugin.addClassList("plugin_class", classList);
        // List<Class<?>> pluginComponents = ntsPlugin.getClassList("spring_component");
        // //List<Class<?>> pluginClassList = ntsPlugin.getClassList().stream().filter(item -> !item.isInterface()).collect(Collectors.toList());
        // if (pluginComponents != null && !pluginComponents.isEmpty()) {
        //     ntsPlugin.getPluginApplicationContext().register(registryClassList.toArray(new Class[registryClassList.size()]));
        // }
    }

    @Override
    public void unRegistry(NtsPlugin ntsPlugin) throws Exception {
        ntsPlugin.clearClassList();
        ntsPlugin.getClassResourceList().clear();
    }

    public void initialize(ApplicationContext applicationContext) {
        groups.add(new PluginComponentGroup());
        MybatisGroup mybatisGroup = new MybatisGroup();
        mybatisGroup.initialize(applicationContext);
        groups.add(mybatisGroup);
        groups.add(new ControllerGroup());
        groups.add(new AgentGroup());
    }
}
