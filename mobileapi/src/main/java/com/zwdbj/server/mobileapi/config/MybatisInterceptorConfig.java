package com.zwdbj.server.mobileapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisInterceptorConfig {

    @Bean
    public MyInterceptor mybatisSqlInterceptor(){
        return  new MyInterceptor();
    }




}