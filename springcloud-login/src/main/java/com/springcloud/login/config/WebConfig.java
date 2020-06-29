package com.springcloud.login.config;

import com.springcloud.login.constant.Constant;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebConfig extends WebMvcConfigurationSupport {

    /**
     * 配置的图片映射
     */
    private static final String imgPath = "file:" + Constant.UPLOAD_PATH + Constant.IMG_FILE_NAME +  "/";

    /**
     * 资源映射配置
     *
     * @param registry
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {

        //将所有访问img/virtual/**的请求映射到文件上传的路径下 C:\Users\wanghao/upload/img（图片的保存路径）
        registry.addResourceHandler("/image/virtual/**").addResourceLocations(imgPath);
        super.addResourceHandlers(registry);
    }
}
