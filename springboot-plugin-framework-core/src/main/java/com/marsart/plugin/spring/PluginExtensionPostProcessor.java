package com.marsart.plugin.spring;

import com.marsart.plugin.SpringPluginManager;
import com.marsart.plugin.annotation.ProxyMethod;
import com.marsart.plugin.extension.inner.ProxyMethodInteceptor;
import com.marsart.plugin.extension.inner.ProxyMethodInteceptorFactory;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;


/**
 * 插件扩展点定义到方法级别 Bean后置处理器
 * 实现内部扩展点的发现 和 扩展点的代理.
 * @author qiyao(1210)
 * @date 2022-03-18
 */
public class PluginExtensionPostProcessor implements BeanPostProcessor {
    private SpringPluginManager springPluginManager;
    private static final Logger logger = LoggerFactory.getLogger(PluginExtensionPostProcessor.class);

    public PluginExtensionPostProcessor(SpringPluginManager manager) {
        this.springPluginManager = manager;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Method[] methods = bean.getClass().getDeclaredMethods();
        List<Method> proxyMethods = new ArrayList<>();
        List<ProxyMethodInteceptor> callbacks = new ArrayList<>();
        for (Method method : methods) {
            ProxyMethod a = method.getAnnotation(ProxyMethod.class);
            if (null != a) {
                proxyMethods.add(method);
                logger.info("PluginExtensionPostProcessor find inner extension ->bean-{}->class-{}->method-{}",
                    beanName, bean.getClass().toString(),method.getName());
                callbacks.add(ProxyMethodInteceptorFactory.getCallBack(bean, method, a.id()));
            }
        }
        if (!proxyMethods.isEmpty()) {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(bean.getClass());
            enhancer.setCallbacks(callbacks.toArray(new Callback[0]));
            if (callbacks.size() > 1) {
                enhancer.setCallbackFilter(ProxyMethodInteceptorFactory.createCallBackFilter(callbacks));
            }
            Object enhanceBean = enhancer.create();
            enhanceAutowire(bean, enhanceBean);
            callbacks.forEach(t -> t.setEnhance(enhanceBean));
            return enhanceBean;
        }
        return bean;
    }

    private void enhanceAutowire(Object originBean, Object enhanceBean) {
        for (Field declaredField : originBean.getClass().getDeclaredFields()) {
            try {
                int modify = declaredField.getModifiers();
                if (!Modifier.isStatic(modify) && !Modifier.isFinal(modify)) {
                    if (!Modifier.isPublic(modify)) {
                        declaredField.setAccessible(true);
                    }
                    declaredField.set(enhanceBean, declaredField.get(originBean));
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

}
