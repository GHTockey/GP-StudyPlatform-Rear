package cn.tockey.service.impl;

import cn.tockey.config.MyQiniuProperties;
import cn.tockey.service.OtherService;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class OtherServiceImpl implements OtherService {
    @Resource
    private MyQiniuProperties myQiniuProperties;

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
}
