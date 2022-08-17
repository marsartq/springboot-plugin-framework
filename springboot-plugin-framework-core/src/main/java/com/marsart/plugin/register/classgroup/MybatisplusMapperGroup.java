package com.marsart.plugin.register.classgroup;

import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.util.ClassUtils;

import com.baomidou.mybatisplus.core.mapper.Mapper;

/**
 * @author qiyao(1210)
 * @date 2022-03-14
 */
public class MybatisplusMapperGroup implements PluginClassGroup {
    @Override
    public String groupId() {
        return "mybatisplus_bean";
    }

    @Override
    public void initialize(ApplicationContext applicationContext) {

    }

    @Override
    public boolean filter(Class<?> aClass) {
        if (!aClass.isInterface()) {
            Set<Class<?>> allInterfacesForClassAsSet = ClassUtils.getAllInterfacesForClassAsSet(aClass);
            return allInterfacesForClassAsSet.contains(Mapper.class);
        } else {
            return Mapper.class.isAssignableFrom(aClass);
        }

    }
}
