package com.zzkj.sales;

import com.zzkj.sales.interceptor.AuthenticationInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * InterceptorRegistry内的addInterceptor需要一个实现HandlerInterceptor接口的拦截器实例，
 * addPathPatterns方法用于设置拦截器的过滤路径规则。
 * 这里我拦截所有请求，通过判断是否有@LoginRequired注解 决定是否需要登录
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Value("${web.upload-path}")
    private String path;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*")
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true).maxAge(3600);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor()).addPathPatterns("/**");
    }
    @Bean
    public AuthenticationInterceptor authenticationInterceptor(){
        return new AuthenticationInterceptor();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/image/**").addResourceLocations("file:E:/image/");
        registry.addResourceHandler("/image/**").addResourceLocations("file:" + path + "/");
    }
}
