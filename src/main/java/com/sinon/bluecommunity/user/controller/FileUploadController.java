package com.sinon.bluecommunity.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/file")
public class FileUploadController {

//    @PostMapping("/uploadAvatar")
//    public Result<String> upload(MultipartFile file) throws Exception {
//        if (file == null || file.isEmpty()) {
//            return Result.error("文件不能为空");
//        }
//        // 获取文件原始名
//        String originalFilename = file.getOriginalFilename();
//        String filename = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));
//        String url = AliOssUtil.uploadFile(filename, file.getInputStream());
//        return Result.success(url);
//    }
}
