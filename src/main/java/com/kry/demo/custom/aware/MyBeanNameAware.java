package com.kry.demo.custom.aware;

/**
 * **********书山有路勤为径**********
 *
 * @author k1rry
 * @date 2021/11/30
 * **********学海无涯苦作舟**********
 */
public interface MyBeanNameAware {

    /**
     * custom bean name
     * @param val
     */
    void setBeanName(String val);

}
