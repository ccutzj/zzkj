package com.zzkj.sales;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
//@SpringBootApplication
//@ComponentScan(basePackages = "com.zzkj.sales.controller")
//@MapperScan(basePackages = "com.zzkj.sales.dao")
public class SalesApplication extends SpringBootServletInitializer {

//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//        return builder.sources(SalesApplication.class);
//    }

    public static void main(String[] args) {
        SpringApplication.run(SalesApplication.class, args);
    }

}
