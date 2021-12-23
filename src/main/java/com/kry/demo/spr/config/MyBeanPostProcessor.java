package com.kry.demo.spr.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class MyBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if ("memberService".equals(beanName)) {
            System.out.println("this is member service initialize before...");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if ("memberService".equals(beanName)) {
            System.out.println("this is member service initialize after...");
        }
        return bean;
    }
}
