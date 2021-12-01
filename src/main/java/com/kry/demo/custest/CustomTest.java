package com.kry.demo.custest;

import com.kry.demo.custest.service.CustomInfoService;
import com.kry.demo.custest.service.CustomService;
import com.kry.demo.custom.context.MyApplicationContext;
import com.kry.demo.custest.config.CustomAppConfig;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * **********书山有路勤为径**********
 *
 * @author k1rry
 * @date 2021/11/30
 * **********学海无涯苦作舟**********
 */
public class CustomTest {

    public static void main(String[] args) {
        MyApplicationContext context = new MyApplicationContext(CustomAppConfig.class);

        CustomInfoService customInfoService = context.getBean(CustomInfoService.class);
        customInfoService.test();

        CustomService customService = (CustomService) context.getBean("customService");
        customService.test();

    }

}
