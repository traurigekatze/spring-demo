package com.kry.demo.spr.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * **********书山有路勤为径**********
 *
 * @author k1rry
 * @date 2021/11/29
 * **********学海无涯苦作舟**********
 */
@Service
public class UserService {

    @Autowired
    private ManService manService;

    @Autowired
    private WomanService womanService;

    public UserService() {
        System.out.println("this is non-params construct...");
    }

    public UserService(ManService manService) {
        this.manService = manService;
        System.out.println("this is manService-params construct...");
    }

    @Autowired
    public UserService(WomanService womanService) {
        this.womanService = womanService;
        System.out.println("this is womanService-params construct...");
    }

    public UserService(ManService manService, WomanService womanService) {
        this.manService = manService;
        this.womanService = womanService;
        System.out.println("this is all-params construct...");
    }

    public UserService(ManService manService, ManService manService3) {
        this.manService = manService;
        System.out.println("this is repeat-params construct...");
    }

    public void test() {
        System.out.println("this UserService test method...");
    }

}
