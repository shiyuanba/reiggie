package com.hjc.reggie.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
@Slf4j
public class WebMvcConfig extends WebMvcConfigurationSupport {
    /**
     * 设置静态资源映射
     * @param registry
     */

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("开启资源映射中...");
        registry.addResourceHandler("/backend/**").addResourceLocations("classPath:/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classPath:/front/");
        log.info("资源映射配置完毕。");
    }
}
