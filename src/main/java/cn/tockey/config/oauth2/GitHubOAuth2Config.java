package cn.tockey.config.oauth2;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "oauth2.github")
@Configuration
public class GitHubOAuth2Config {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
}
