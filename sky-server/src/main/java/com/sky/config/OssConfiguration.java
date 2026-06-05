package com.sky.config;

import com.sky.properties.AliOssProperties;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OssConfiguration {
    @Bean
    public AliOssUtil aliOssUtil(AliOssProperties   aliOssProperties){
        log.info("开始创建阿里云文件上传工具类对象");
        AliOssUtil aliOssUtil=new AliOssUtil(aliOssProperties.getEndpoint(),aliOssProperties.getAccessKeyId(),
                aliOssProperties.getAccessKeySecret(),aliOssProperties.getBucketName());
        return aliOssUtil;
    }
}
