package cn.tockey.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserRegVo {
    private String username;
    private String password;
    private String avatar;
    private String email;
}
