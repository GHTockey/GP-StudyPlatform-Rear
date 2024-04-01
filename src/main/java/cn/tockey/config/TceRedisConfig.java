package cn.tockey.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "tce.redis")
public class TceRedisConfig {
    private String emailCodeKey;
}
