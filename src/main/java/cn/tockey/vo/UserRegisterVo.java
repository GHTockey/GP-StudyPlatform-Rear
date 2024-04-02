package cn.tockey.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserRegisterVo {
    private String username;
    private String password;
    private String email;
    private String code;
}
