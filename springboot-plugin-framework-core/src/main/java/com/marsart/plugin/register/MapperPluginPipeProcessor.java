package com.marsart.plugin.register;

import java.util.List;
import java.util.Objects;

import com.marsart.plugin.NtsPlugin;
import com.marsart.plugin.PluginPipeProcessor;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.core.NestedIOException;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;


/**
 * @author qiyao(1210)
 * @date 2022-03-09
 */
public class MapperPluginPipeProcessor implements PluginPipeProcessor {

    private static final Logger logger = LoggerFactory.getLogger(MapperPluginPipeProcessor.class);

    @Override
    public void initialize() {

    }

    @Override
    public void registry(NtsPlugin ntsPlugin) throws Exception {
        List<Class<?>> mapperClassList = ntsPlugin.getClassList("mapper");
        if (mapperClassList == null || mapperClassList.isEmpty()) {
            return;
        }

        //注册mapper
        for (Class<?> mapperClass : mapperClassList) {
            GenericBeanDefinition definition = new GenericBeanDefinition();
            definition.getConstructorArgumentValues().addGenericArgumentValue(mapperClass);
            definition.setBeanClass(MapperFactoryBean.class);
            definition.getPropertyValues().add("addToConfig", true);
            definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
            ntsPlugin.getPluginApplicationContext().registerBeanDefinition(mapperClass.getName(), definition);
            logger.info("MapperPluginPipeProcessor-plugin-[{}]  mybatis bean:[{}] instantiation.", ntsPlugin.getPluginId(), mapperClass.getName());
        }

        //注册mapper.xml
        SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) ntsPlugin.getMainApplicationContext().getBean("sqlSessionFactory");
        Configuration configuration = sqlSessionFactory.getConfiguration();
        try {
            Resources.setDefaultClassLoader(ntsPlugin.getPluginWrapper().getPluginClassLoader());
            for (Resource mapperXmlResource : ntsPlugin.getMapperXmlResourceList()) {
                if (mapperXmlResource == null) {
                    continue;
                }

                if (!Objects.requireNonNull(mapperXmlResource.getFilename()).endsWith("Mapper.xml")) {
                    continue;
                }

                try {
                    XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(mapperXmlResource.getInputStream(),
                        configuration, mapperXmlResource.toString(), configuration.getSqlFragments());
                    xmlMapperBuilder.parse();
                    logger.info("MapperPluginPipeProcessor-plugin-[{}]  mybatis mapper-xml:[{}] registered.", ntsPlugin.getPluginId(), mapperXmlResource.getFilename());
                } catch (Exception e) {
                    throw new NestedIOException("Failed to parse mapping resource: '" + mapperXmlResource + "'", e);
                } finally {
                    ErrorContext.instance().reset();
                }
            }
        } finally {
            Resources.setDefaultClassLoader(ClassUtils.getDefaultClassLoader());
        }

    }

    @Override
    public void unRegistry(NtsPlugin ntsPlugin) throws Exception {
        List<Class<?>> mapperClassList = ntsPlugin.getClassList("mapper");
        if (mapperClassList.isEmpty()) {
            return;
        }

        //卸载mapper
        //SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) ntsPlugin.getMainApplicationContext().getBean("sqlSessionFactory");
        //Configuration configuration = sqlSessionFactory.getConfiguration();
        for (Class<?> baseMapperClass : mapperClassList) {
            ntsPlugin.getPluginApplicationContext().removeBeanDefinition(baseMapperClass.getName());
        }
    }

}
