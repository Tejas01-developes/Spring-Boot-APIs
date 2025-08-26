package com.practice.keepgowing.webconfig;

import org.springframework.beans.factory.annotation.Autowired;
import com.practice.keepgowing.filter.filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

public class webconfig  {
    @Autowired
    private filter filter;

    @Bean
    public FilterRegistrationBean<filter> filterRegistrationBean(){
        FilterRegistrationBean<filter> registrationBean=new FilterRegistrationBean<>();
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns("/*");
        return registrationBean;



    }






}
