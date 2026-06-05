package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RequestMapping("/admin/common")
@Slf4j
@RestController
public class CommonController {
    @Autowired
    private AliOssUtil aliOssUtil;
    @PostMapping("/upload")
    public Result upload(MultipartFile file) {
        String url= null;
        try {
            String originalFilename=file.getOriginalFilename();
            String suffix=originalFilename.substring(originalFilename.lastIndexOf("."));
            String objectName= UUID.randomUUID().toString()+suffix;
            url = aliOssUtil.upload(file.getBytes(),objectName);
        } catch (IOException e) {
            return Result.error("文件上传失败");
        } finally {
        }
        log.info("url:{}",url);
        return Result.success(url);
    }
}
