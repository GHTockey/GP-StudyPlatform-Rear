package cn.tockey.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface OtherService{
    // 图片上传(七牛云)
    String uploadImage(MultipartFile file) throws IOException;
}
