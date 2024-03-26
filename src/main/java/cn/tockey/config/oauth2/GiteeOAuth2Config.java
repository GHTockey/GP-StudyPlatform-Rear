package cn.tockey.config.oauth2;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties(prefix = "oauth2.gitee")
@Configuration
public class GiteeOAuth2Config {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String authorizeUrl;
}
