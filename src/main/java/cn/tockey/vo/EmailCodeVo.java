package cn.tockey.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class EmailCodeVo {
    private String email;
    private String code;
    private Date expireTime;
}