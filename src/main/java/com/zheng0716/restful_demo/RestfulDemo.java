package com.zheng0716.restful_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 入口类.
 * Created by zhengxiaoyao0716 on 2016/9/11.
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
@ServletComponentScan
public class RestfulDemo {
    public static void main(String[] args) {
        SpringApplication.run(RestfulDemo.class, args);
    }
}
