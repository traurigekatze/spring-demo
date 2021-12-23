package com.kry.demo.spr.config;

import com.kry.demo.spr.service.MemberService;
import com.kry.demo.spr.service.User;
import com.kry.demo.spr.service.UserService;
import org.springframework.beans.SimpleTypeConverter;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.CustomEditorConfigurer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.convert.support.DefaultConversionService;

import java.beans.PropertyEditor;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * **********书山有路勤为径**********
 *
 * @author k1rry
 * @date 2021/11/29
 * **********学海无涯苦作舟**********
 */
//@PropertySource("classpath:application.properties")
@ComponentScan(basePackages = "com.kry.demo.spr"
//        , includeFilters = {@ComponentScan.Filter(
//                type = FilterType.ASSIGNABLE_TYPE,
//                classes = MyFactoryBean.class)},
//        excludeFilters = {@ComponentScan.Filter(
//                type = FilterType.ASSIGNABLE_TYPE,
//                classes = UserService.class
//        )}
)
public class AppConfig {

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        return messageSource;
    }

    @Bean
    public ApplicationListener applicationListener() {
        return event -> {
            if (event instanceof OrderEvent) {
                OrderEvent orderEvent = (OrderEvent) event;
                System.out.println("接收了一个事件，内容：" + orderEvent.getMessage());
            }
        };
    }

    @Bean
    public CustomEditorConfigurer customEditorConfigurer() {
        CustomEditorConfigurer customEditorConfigurer = new CustomEditorConfigurer();
        Map<Class<?>, Class<? extends PropertyEditor>> propertyEditorMap = new HashMap<>();
        propertyEditorMap.put(User.class, StringToUserPropertyEditor.class);
        customEditorConfigurer.setCustomEditors(propertyEditorMap);
        return customEditorConfigurer;
    }

    @Bean
    public ConversionServiceFactoryBean conversionService() {
        ConversionServiceFactoryBean conversionService = new ConversionServiceFactoryBean();
        conversionService.setConverters(Collections.singleton(new StringToUserConverter()));
        return conversionService;
    }

    @Bean
    public TypeConverter typeConverter() {
        SimpleTypeConverter typeConverter = new SimpleTypeConverter();
        DefaultConversionService conversionService = new DefaultConversionService();
        conversionService.addConverter(new StringToUserConverter());
        typeConverter.setConversionService(conversionService);
//        typeConverter.registerCustomEditor(User.class, new StringToUserPropertyEditor());
        return typeConverter;
    }

}
