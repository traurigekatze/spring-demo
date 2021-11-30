package com.kry.demo.custest;

import com.kry.demo.custom.context.MyApplicationContext;
import com.kry.demo.custest.config.CustomAppConfig;

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
    }

}
