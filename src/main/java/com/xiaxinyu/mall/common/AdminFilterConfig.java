package com.xiaxinyu.mall.common;

import com.xiaxinyu.mall.filter.AdminFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

/**
 * @Description:
 * @author: xiaxinyu
 * @Email: xiaxinyuxy@163.com
 * @date: 2022年12月04日 21:08
 * @Copyright: 个人版权所有
 * @version: 1.0.0
 */

@Configuration
public class AdminFilterConfig {
    @Bean
    public AdminFilter adminFilter(){
        return new AdminFilter();
    }

    @Bean(name = "adminFilterConf")
    public FilterRegistrationBean adminFilterConfig(){
        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(adminFilter());
        filterFilterRegistrationBean.addUrlPatterns("/admin/category/*");
        filterFilterRegistrationBean.addUrlPatterns("/admin/product/*");
        filterFilterRegistrationBean.addUrlPatterns("/admin/order/*");
        filterFilterRegistrationBean.setName("adminFilterConfig");
        return filterFilterRegistrationBean;
    }
}