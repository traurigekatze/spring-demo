package com.kry.demo.custom.config;

import com.kry.demo.custom.enums.ScopeEnum;

/**
 * **********书山有路勤为径**********
 *
 * @author k1rry
 * @date 2021/11/30
 * **********学海无涯苦作舟**********
 */
public class MyBeanDefinition {

    private Class<?> beanClass;

    private ScopeEnum scope;

    private boolean lazy;

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    public ScopeEnum getScope() {
        return scope;
    }

    public void setScope(ScopeEnum scope) {
        this.scope = scope;
    }

    public boolean isLazy() {
        return lazy;
    }

    public void setLazy(boolean lazy) {
        this.lazy = lazy;
    }
}
