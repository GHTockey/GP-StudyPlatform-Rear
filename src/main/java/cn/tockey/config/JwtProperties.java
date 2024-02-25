package cn.tockey.config;

import cn.tockey.utils.RsaUtils;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author 桐叔
 * @email liangtong@itcast.cn
 * @description
 */
@Data
@ConfigurationProperties(prefix = "tce.jwt")
@Component
public class JwtProperties {
    private String secret; // 密钥

    private String pubKeyPath;// 公钥

    private String priKeyPath;// 私钥

    private int expire;// token过期时间

    private PublicKey publicKey; // 公钥

    private PrivateKey privateKey; // 私钥

    @PostConstruct            //当前类加载到spring容器时，执行（初始化操作）
    public void init(){
        try {
            File pubFile = new File(this.pubKeyPath);
            File priFile = new File(this.priKeyPath);
            //如果公钥或私钥文件不存在，则自动生成公钥私钥文件
            if( !pubFile.exists() || !priFile.exists()){
                RsaUtils.generateKey( this.pubKeyPath ,this.priKeyPath , this.secret);
            }
            //根据公钥或私钥路径，生成对应的对象
            this.publicKey = RsaUtils.getPublicKey( this.pubKeyPath );
            this.privateKey = RsaUtils.getPrivateKey( this.priKeyPath );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
