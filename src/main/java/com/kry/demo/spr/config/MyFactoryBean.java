package com.kry.demo.spr.config;

import com.kry.demo.spr.service.User;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

//@Component
public class MyFactoryBean implements FactoryBean {

    @Override
    public Object getObject() throws Exception {
        return new User();
    }

    @Override
    public Class<?> getObjectType() {
        return User.class;
    }
}
