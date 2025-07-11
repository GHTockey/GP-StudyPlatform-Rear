package cn.tockey.service.impl;

import cn.tockey.config.MyQiniuProperties;
import cn.tockey.config.TceRedisConfig;
import cn.tockey.service.OtherService;
import cn.tockey.vo.EmailCodeVo;
import com.alibaba.fastjson2.JSON;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class OtherServiceImpl implements OtherService {
    private static final String SENDER = "tockey@yeah.net"; // 发送者;
    @Resource
    private MyQiniuProperties myQiniuProperties;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private JavaMailSender mailSender;

    @Override
    public String uploadImage(MultipartFile file) throws IOException {
        Configuration cfg = new Configuration(Region.region2());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;

        UploadManager uploadManager = new UploadManager(cfg);
        String accKey = myQiniuProperties.getAk();
        String secKey = myQiniuProperties.getSk();
        String bucket = myQiniuProperties.getBucket();

        Auth auth = Auth.create(accKey, secKey);
        String upToken = auth.uploadToken(bucket);

        // UUID 文件名
        String fileName = UUID.randomUUID().toString().replace("-", "") + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        //System.out.println(fileName);

        // 调用put方法上传
        Response response = uploadManager.put(file.getInputStream(), myQiniuProperties.getPath() + fileName, upToken, null, null);
        // 打印返回的信息
        //System.out.println(response.bodyString()+"========="+response.getInfo());
        return myQiniuProperties.getBaseUrl() + myQiniuProperties.getPath() + fileName;
    }

    // 获取 OAuth 用户信息 serviceImpl
    @Override
    public <T> T getOAuthUserInfo(String oKey, String type, Class<T> clazz) {
        String OAuthUserStr = stringRedisTemplate.opsForValue().get(oKey);
        return JSON.parseObject(OAuthUserStr, clazz);
    }

    // 发送邮件验证码 返回验证码过期时间 serviceImpl
    @Override
    public EmailCodeVo sendEmailVerificationCode(String to) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            // 生成验证码
            String code = String.valueOf((int) ((Math.random() * 9 + 1) * 1000));

            // 发送邮件
            //true表示需要创建一个multipart message
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(SENDER);
            helper.setTo(to);
            helper.setSubject("智词领航-验证码");
            helper.setText("<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "   <meta charset=\"UTF-8\">\n" +
                    "   <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "   <title>Document</title>\n" +
                    "   <style>\n" +
                    "      * {\n" +
                    "         margin: 0;\n" +
                    "         padding: 0;\n" +
                    "      }\n" +
                    "\n" +
                    "      html,\n" +
                    "      body,\n" +
                    "      .container {\n" +
                    "         width: 100%;\n" +
                    "         height: 100%;\n" +
                    "      }\n" +
                    "   </style>\n" +
                    "</head>\n" +
                    "\n" +
                    "<body>\n" +
                    "   <div class=\"container\" style=\"display: grid; place-content: center; padding: 100px 0;\">\n" +
                    "      <div class=\"box\"\n" +
                    "         style=\"box-sizing: border-box;padding: 20px;width: 450px;border-radius: 10px;box-shadow: rgba(17, 17, 26, 0.1) 0px 4px 16px, rgba(17, 17, 26, 0.05) 0px 8px 32px;\">\n" +
                    "         <img src=\"https://img.tockey.cn/pic/logo.svg\" alt=\"logo\" style=\"width: 110px;\">\n" +
                    "         <div style=\"margin-top: 20px;\">\n" +
                    "            <!-- <p>Hi；<span style=\"color: rgb(0, 127, 177);\">XXX</span>!</p> -->\n" +
                    "            <p style=\"font-size: 16px;font-weight: 600;\">您的验证码为</p>\n" +
                    "            <div class=\"code\"\n" +
                    "               style=\"margin: 10px 0;color: rgb(0, 183, 255);font-size: 23px;font-weight: 600;border-radius: 8px;padding: 10px 0;text-align: center;background-color: rgb(228, 228, 228);\">\n" +
                    "               "+ code +"</div>\n" +
                    "            <p style=\"font-size: 15px;color: gray;\">请在指定页面输入验证码完成验证。</p>\n" +
                    "            <p style=\"font-size: 15px;color: gray;\">如非本人操作，请忽略此邮件。</p>\n" +
                    "         </div>\n" +
                    "      </div>\n" +
                    "   </div>\n" +
                    "</body>\n" +
                    "</html>", true);
            mailSender.send(message);

            // 缓存 1分钟
            //stringRedisTemplate.opsForValue().set(tceRedisConfig.getEmailCodeKey() + to, code, 1, TimeUnit.MINUTES);
            EmailCodeVo emailCodeVo = new EmailCodeVo(to, code, new Date(System.currentTimeMillis() + 60000));
            stringRedisTemplate.opsForValue().set(TceRedisConfig.emailCodeKey + to, JSON.toJSONString(emailCodeVo), 1, TimeUnit.MINUTES);
            return emailCodeVo;
        } catch (MessagingException e) {
            System.out.println("发送MimeMessage时发生异常！" + e.getMessage());
        }
        return null;
    }

}
