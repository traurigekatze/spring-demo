package com.kry.demo.custom.exception;

/**
 * **********书山有路勤为径**********
 *
 * @author k1rry
 * @date 2021/12/01
 * **********学海无涯苦作舟**********
 */
public class MyNoUniqueBeanDefinitionException extends Error {

    public MyNoUniqueBeanDefinitionException(String message) {
        super(message);
    }
}