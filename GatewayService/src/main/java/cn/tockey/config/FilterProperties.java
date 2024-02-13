package cn.tockey.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "tce.filter") // 读取配置文件中的前缀为tce.filter的配置
public class FilterProperties {
    // 读取配置文件中的allowPaths
    private List<String> allowPaths;
}
