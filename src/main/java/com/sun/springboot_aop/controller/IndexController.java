package com.sun.springboot_aop.controller;

import com.sun.springboot_aop.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * All rights Reserved, Designed By www.maihaoche.com
 *
 * @Package com.sun.springboot_aop.controller
 * @author: angtai（angtai@maihaoche.com）
 * @date: 2020/3/12 4:41 PM
 * @Copyright: 2017-2020 www.maihaoche.com Inc. All rights reserved.
 */
@Slf4j
@RestController
public class IndexController {

    @RequestMapping("/hello")
    public String test(){
        log.info("========================Advice方法======================");
        User user = new User();
        String s = user.getName().toString();
        return "213";
    }

    @RequestMapping("/hello2")
    public void test2(){

        System.out.println("123");
    }
}