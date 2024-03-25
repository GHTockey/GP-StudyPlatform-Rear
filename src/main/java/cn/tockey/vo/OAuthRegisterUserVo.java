package cn.tockey.vo;

import lombok.Data;

@Data
public class OAuthRegisterUserVo {
    private String username;
    private String password;
    private String avatar;
    private String email;
}
