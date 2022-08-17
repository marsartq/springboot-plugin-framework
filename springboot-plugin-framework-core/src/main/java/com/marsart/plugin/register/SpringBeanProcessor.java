package com.marsart.plugin.register;

import com.marsart.plugin.NtsPlugin;
import com.marsart.plugin.PluginPipeProcessor;
import com.marsart.plugin.register.classgroup.SchedulingGroup;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;

import java.util.List;


/**
 * @author qiyao(1210)
 * @date
 */
public class SpringBeanProcessor implements PluginPipeProcessor {
    @Override
    public void initialize() {

    }

    @Override
    public void registry(NtsPlugin ntsPlugin) throws Exception {
        List<Class<?>> classes = ntsPlugin.getClassList("plugin_component");
        if (classes.isEmpty()) {
            return;
        }
        SchedulingGroup schedulingGroup = new SchedulingGroup();
        // @EnableScheduling
        if (classes.stream().anyMatch(schedulingGroup::filter)
        || schedulingGroup.filter(ntsPlugin.getPluginWrapper().getPlugin().getClass())) {
            ntsPlugin.getPluginApplicationContext().register(ScheduledAnnotationBeanPostProcessor.class);
        }
        ntsPlugin.getPluginApplicationContext().register(classes.toArray(new Class[0]));
    }

    @Override
    public void unRegistry(NtsPlugin ntsPlugin) throws Exception {
        ntsPlugin.getPluginApplicationContext().close();
    }
}
