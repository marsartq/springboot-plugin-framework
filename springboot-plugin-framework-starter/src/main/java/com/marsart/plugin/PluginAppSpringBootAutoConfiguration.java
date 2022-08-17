package com.marsart.plugin;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({PluginAutoConfiguration.class})
public class PluginAppSpringBootAutoConfiguration {
}
