package com.myhome.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;

@Configuration
public class FileUploadConfig {

    @Value("${files.maxSizeKBytes}")
    private int maxSizeKBytes;

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofKilobytes(maxSizeKBytes));
        factory.setMaxRequestSize(DataSize.ofKilobytes(maxSizeKBytes));
        return factory.createMultipartConfig();
    }



}
