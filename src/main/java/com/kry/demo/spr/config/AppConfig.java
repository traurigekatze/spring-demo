package com.kry.demo.spr.config;

import com.kry.demo.spr.service.ManService;
import com.kry.demo.spr.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * **********书山有路勤为径**********
 *
 * @author k1rry
 * @date 2021/11/29
 * **********学海无涯苦作舟**********
 */
@ComponentScan(basePackages = "com.kry.demo.spr")
public class AppConfig {

    @Bean
    public ManService manService2() {
        return new ManService();
    }
}
