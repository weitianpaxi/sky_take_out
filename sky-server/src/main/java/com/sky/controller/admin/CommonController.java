package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * 通用接口
 * @author paxi
 * @data 2023/8/27
 **/
@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
@Slf4j
public class CommonController {

    @Autowired
    private AliOssUtil aliOssUtil;

    /**
     * 文件上传
     * @param file 参数名称务必与前端请求体中所定义的参数一致
     * @return com.sky.result.Result<java.lang.String>
     * @author paxi
     * @data 2023/8/27
     **/
    @PostMapping("/upload")
    @ApiOperation(value = "文件上传接口")
    public Result<String> upload(MultipartFile file) {
        log.info("文件上传：{}",file);

        // 获取原始文件名
        String originalFilename = file.getOriginalFilename();
        // 截取文件扩展名
        String extensionName = null;
        if (originalFilename != null) {
            extensionName = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        // 利用UUID拼接存储在OSS中的文件名，防止重名文件
        String objectName = UUID.randomUUID().toString() + extensionName;
        try {
            // 文件的请求路径
            String filePath = aliOssUtil.upload(file.getBytes(), objectName);
            return Result.success(filePath);
        } catch (IOException e) {
            log.error("文件上传失败：{}",e);
            //throw new RuntimeException(e);
        }
        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}
