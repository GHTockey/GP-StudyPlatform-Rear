package cn.tockey.controller;

import cn.tockey.service.OtherService;
import cn.tockey.vo.BaseResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/other")
public class OtherController {
    @Resource
    private OtherService otherService;

    @PostMapping("/image/upload")
    BaseResult<String> uploadImage( MultipartFile file) {
        try {
            String updRes = otherService.uploadImage(file);
            return BaseResult.ok("上传成功", updRes);
        } catch (IOException e) {
            e.printStackTrace();
            return BaseResult.error("上传失败");
        }

    }
}
