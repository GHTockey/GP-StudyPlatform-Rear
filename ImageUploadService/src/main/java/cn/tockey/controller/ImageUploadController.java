package cn.tockey.controller;

import cn.tockey.service.ImageUploadService;
//import cn.tockey.vo.BaseResult;
import cn.tockey.vo.BaseResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/image")
public class ImageUploadController {
    @Resource
    private ImageUploadService imageUploadService;

    @PostMapping("/upload")
    BaseResult<String> uploadImage(MultipartFile file) {
        //String string = imageUploadService.uploadImage(file.getInputStream(), file.getOriginalFilename());
        //String originalFilename = file.getOriginalFilename();
        //System.out.println(file.getInputStream()+"----"+ originalFilename);

        //String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //String fileKey = UUID.randomUUID().toString() + suffix;
        //System.out.println(fileKey);

        try {
            String updRes = imageUploadService.uploadImage(file);
            return BaseResult.ok("上传成功", updRes);
        } catch (IOException e) {
            return BaseResult.error("上传失败");
        }

    }
}
