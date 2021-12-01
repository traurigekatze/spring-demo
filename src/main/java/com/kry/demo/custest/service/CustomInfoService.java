package com.kry.demo.custest.service;

import com.kry.demo.custom.annotation.MyAutowired;
import com.kry.demo.custom.annotation.MyComponent;

/**
 * **********书山有路勤为径**********
 *
 * @author k1rry
 * @date 2021/11/30
 * **********学海无涯苦作舟**********
 */
@MyComponent
public class CustomInfoService {

    public CustomInfoService() {
        System.out.println("this custom info service constructor...");
    }

    public void test() {
        System.out.println("this is custom info test method..." + this);
    }
}
