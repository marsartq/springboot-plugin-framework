/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * 插件 加载和卸载的处理类 接口
 * @author qiyao(1210)
 * @date 2022-03-09
 */
package com.marsart.plugin;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.marsart.plugin.controller.PluginManagerController;
import com.marsart.plugin.spring.PluginBeanFactoryPostProcessor;
import com.marsart.plugin.spring.PluginExtensionPostProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;


/**
 * @author： wjun_java@163.com
 * @date： 2021/4/27
 * @description：
 * @modifiedBy：
 * @version: 1.0
 */
@Configuration
@Import({PluginExtensionPostProcessor.class, PluginBeanFactoryPostProcessor.class})
public class PluginAutoConfiguration implements EnvironmentAware {

    public PluginAutoConfiguration() {
        //System.out.println("load PluginAutoConfiguration");
    }

    private Environment environment;

    private Path path;

    @Value("${plugin.path:plugins}")
    public void setPath(String path) {
        this.path = Paths.get(path);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public PluginManagerController pluginManagerController() {
        return new PluginManagerController();
    }
}
