package com.kry.demo.custom.exception;

/**
 * **********书山有路勤为径**********
 *
 * @author k1rry
 * @date 2021/11/30
 * **********学海无涯苦作舟**********
 */
public class MyNoSuchClassException extends RuntimeException {

    public MyNoSuchClassException(String message) {
        super(message);
    }
}
