package com.marsart.plugin.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 暂时无用.
 * @author qiyao(1210)
 * @date 2022-03-18
 */
@Deprecated
public class PluginFrameworkInitializer implements ApplicationContextInitializer {
    private static Logger logger = LoggerFactory.getLogger(PluginFrameworkInitializer.class);
    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        logger.info("PluginFrameworkInitializer-initialize...");
    }
}
