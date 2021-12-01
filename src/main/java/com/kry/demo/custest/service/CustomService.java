package com.kry.demo.custest.service;

import com.kry.demo.custom.annotation.MyAutowired;
import com.kry.demo.custom.annotation.MyComponent;
import org.springframework.context.annotation.Lazy;

/**
 * **********书山有路勤为径**********
 *
 * @author k1rry
 * @date 2021/11/30
 * **********学海无涯苦作舟**********
 */
@Lazy
@MyComponent
public class CustomService {

    @MyAutowired
    private CustomInfoService customInfoService;

    @MyAutowired
    public CustomService(CustomInfoService customInfoService) {
        this.customInfoService = customInfoService;
        System.out.println("this is custom service constructor params " + customInfoService + "...");
    }

    public void test() {
        System.out.println("this is custom test method..." + this);
    }
}
