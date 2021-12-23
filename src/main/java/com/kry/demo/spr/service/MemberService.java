package com.kry.demo.spr.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    @Value("kkry")
    private User user;


    public void init() {
        System.out.println("this is member service init method...");
    }

    public void test() {
        System.out.println("this is member service...");
    }

    public void user() {
        System.out.println("user name is: " + user.getName());
    }

}
