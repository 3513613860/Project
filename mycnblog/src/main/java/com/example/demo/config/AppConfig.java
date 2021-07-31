package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig  implements WebMvcConfigurer {
    @Value("${myimgpath}")
    private String imgpath;

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        //给所有的接口添加api前缀
        configurer.addPathPrefix("api",c->true);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**").
                addResourceLocations("file:"+imgpath);
    }

    //配置拦截规则
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**")   //拦截所有的接口
                .excludePathPatterns("/api/userlogin/login")  //不拦截登录接口
                .excludePathPatterns("/api/user/register") //不拦截注册接口
                .excludePathPatterns("/api/user/test")
                .excludePathPatterns("/login.html")
                .excludePathPatterns("/register.html")
                .excludePathPatterns("/reg_success.html")
                .excludePathPatterns("/reg_error.html")
                .excludePathPatterns("/blog_list.html")
                .excludePathPatterns("/myblog_list.html")
                //.excludePathPatterns("/**/**.html")    //不拦截所有html页面
                .excludePathPatterns("/**/*.css")
                .excludePathPatterns("/**/*.js")
                .excludePathPatterns("/**/*.jpg")
                .excludePathPatterns("/**/*.png");

    }
}
