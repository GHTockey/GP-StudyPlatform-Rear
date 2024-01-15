package cn.tockey.service;

import com.qiniu.common.QiniuException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public interface ImageUploadService {
    // 图片上传(七牛云)
    String uploadImage(MultipartFile file) throws IOException;
}
