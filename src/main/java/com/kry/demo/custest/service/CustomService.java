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
public class CustomService {

    @MyAutowired
    private CustomInfoService customInfoService;

    public CustomService() {
    }

    @MyAutowired
    public CustomService(CustomInfoService customInfoService) {
        this.customInfoService = customInfoService;
    }
}
