package com.marsart.plugin.spring;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.marsart.plugin.SpringPluginManager;
import com.marsart.plugin.register.DefaultPluginPipeProcessor;
import org.pf4j.AbstractPluginManager;
import org.pf4j.RuntimeMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;



/**
 * SpringApplicationRunListener.
 * contextPrepared后,创建 pluginManager并loader当前默认插件.
 * started之后初始化所有的插件功能.
 * @author qiyao(1210)
 * @date 2022-03-18
 */
public class PluginSpringApplicationRunListener implements SpringApplicationRunListener {
    private static final Logger logger = LoggerFactory.getLogger(PluginSpringApplicationRunListener.class);

    private Path pluginPath;

    private static final String DEFAULT_PATH = "plugins";

    private final SpringApplication application;

    private SpringPluginManager springPluginManager;

    private final String[] args;

    public PluginSpringApplicationRunListener(SpringApplication springApplication, String[] args) {
        this.application = springApplication;
        this.args = args;
    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
        Set<String> profile = new HashSet<>(Arrays.asList(environment.getActiveProfiles()));
        // if (profile.contains("dev")) {
        //     System.setProperty(AbstractPluginManager.MODE_PROPERTY_NAME, RuntimeMode.DEVELOPMENT.toString());
        // }
        // 不使用 dev方式
        System.setProperty(AbstractPluginManager.MODE_PROPERTY_NAME, RuntimeMode.DEPLOYMENT.toString());
        String path = environment.getProperty("plugin.path", DEFAULT_PATH);
        pluginPath = Paths.get(path);
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        logger.info("PluginSpringApplicationRunListener contextPrepared, init plugin manager.");
        DefaultPluginPipeProcessor pluginPipeProcessor = new DefaultPluginPipeProcessor();
        pluginPipeProcessor.setApplicationContext(context);
        // context.getBeanFactory().registerSingleton("PluginPipeProcessor", pluginPipeProcessor);
        springPluginManager = createPluginManager();
        springPluginManager.setApplicationContext(context);
        springPluginManager.setPluginPipeProcessor(pluginPipeProcessor);
        springPluginManager.init();
        context.getBeanFactory().registerSingleton("PluginManager", springPluginManager);
    }

    @Override
    public void started(ConfigurableApplicationContext applicationContext) {
        logger.info("PluginSpringApplicationRunListener application started, auto init plugins.");
        try {
            springPluginManager.initPlugins();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    private SpringPluginManager createPluginManager() {
        return new SpringPluginManager(pluginPath);
    }
}
