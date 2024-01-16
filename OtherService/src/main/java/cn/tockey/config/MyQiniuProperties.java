package cn.tockey.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration // 该注解表示该类是一个配置类
@ConfigurationProperties(prefix = "tce.qiniu") // 映射配置文件中的tce.qiniu前缀的配置
public class MyQiniuProperties {
    private String ak;
    private String sk;
    private String bucket;
    private String baseUrl;
    private String path;
}
