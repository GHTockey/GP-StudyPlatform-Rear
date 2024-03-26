package cn.tockey.vo;

import lombok.Data;


// gitee 授权登录返回的数据
@Data
public class GiteeAccessVo {
    private String access_token;
    private String token_type;
    private String expires_in;
    private String refresh_token;
    private String scope;
    private String created_at;
}
