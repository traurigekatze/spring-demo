package com.kry.demo.spr.config;

import org.springframework.context.ApplicationEvent;

public class OrderEvent extends ApplicationEvent {

    private String message;

    public OrderEvent(Object source, String message) {
        super(source);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
