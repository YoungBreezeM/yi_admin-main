package com.fw.wx.config;


import com.fw.wx.interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.web.servlet.config.annotation.*;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author yqf
 */
@Configuration
public class WxConfiguration extends WebMvcConfigurationSupport {

    @Autowired
    private TokenInterceptor tokenInterceptor;

    /**
     * 解决跨域请求
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedHeaders("*")
                .allowedMethods("*")
                .allowedOrigins("*")
                .allowCredentials(true);
    }

    /**
     * 异步请求配置
     *
     * @param configurer
     */
    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                10,
                10,
                5,
                TimeUnit.MINUTES,
                new LinkedBlockingQueue<>(),
                r -> new Thread(r, "-Thread_")
        );
        configurer.setTaskExecutor(new ConcurrentTaskExecutor(threadPoolExecutor));
        configurer.setDefaultTimeout(30000);
    }


    /**
     * 配置拦截器、拦截路径
     * 每次请求到拦截的路径，就会去执行拦截器中的方法
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {


        registry
                .addInterceptor(tokenInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/client/login/**")
                .excludePathPatterns("/uploads/**");

    }


    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/uploads/**")
                .addResourceLocations("classpath:/uploads/");
    }
}

