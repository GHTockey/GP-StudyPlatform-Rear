package cn.tockey.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

// 统一 Redis key 的前缀

@Data
//@Component
//@ConfigurationProperties(prefix = "tce.redis")
public class TceRedisConfig {
    // 邮箱验证码
    public static String emailCodeKey = "emailCode:";
    // 邮箱验证码发送次数
    public static String emailCodeSendCountKey = "emailCodeSendCount:";
}
