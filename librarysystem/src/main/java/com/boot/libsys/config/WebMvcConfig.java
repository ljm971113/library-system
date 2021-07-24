package com.boot.libsys.config;

import com.boot.libsys.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Lucky
 * @version 1.0
 * @since 2020/06/10 10:58
 */

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/**")
                .excludePathPatterns("/","/login","/userLogin","/toRegister","/error_403","/register",
                        "/common/checkUsername")
        //配置静态资源不拦截
        .excludePathPatterns("/static/assets/**")
        .excludePathPatterns("/static/comment/**")
        .excludePathPatterns("/static/login/**")
        .excludePathPatterns("/static/public/**")
        .excludePathPatterns("/static/xadmin/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }

}
