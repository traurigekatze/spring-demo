package com.kry.demo.spr;

import com.kry.demo.spr.config.AppConfig;
import com.kry.demo.spr.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * **********书山有路勤为径**********
 *
 * @author k1rry
 * @date 2021/11/29
 * **********学海无涯苦作舟**********
 */
public class SprTests {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        UserService userService = (UserService) context.getBean("my_name");
        userService.test();
    }

}
