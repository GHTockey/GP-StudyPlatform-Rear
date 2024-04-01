package cn.tockey.service;

import cn.tockey.vo.EmailCodeVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

public interface OtherService{
    // 图片上传(七牛云)
    String uploadImage(MultipartFile file) throws IOException;
    // 获取 OAuth 用户信息
    <T> T getOAuthUserInfo(String oKey, String type, Class<T> clazz);
    // 发送邮件验证码 返回验证码过期时间
    EmailCodeVo sendEmailVerificationCode(String to);
}
