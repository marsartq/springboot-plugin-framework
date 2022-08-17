package com.marsart.plugin.register.classgroup;

import com.marsart.plugin.annotation.AgentMethod;
import com.marsart.plugin.utils.AnnotationsUtils;
import org.springframework.context.ApplicationContext;


/**
 * 实现扩展点的Class组
 * @author qiyao(1210)
 * @date 2022-04-07
 */
public class AgentGroup implements PluginClassGroup {

    @Override
    public String groupId() {
        return "agents";
    }

    @Override
    public void initialize(ApplicationContext applicationContext) {

    }

    @Override
    public boolean filter(Class<?> aClass) {
        return AnnotationsUtils.haveMethodAnnotations(aClass, AgentMethod.class);
    }
}
