package com.kry.demo.spr;

import com.kry.demo.spr.config.AppConfig;
import com.kry.demo.spr.service.*;
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
        System.out.println(SprTests.class.getName());
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        context.getBean(MemberService.class).test();
    }

}
