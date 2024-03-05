package cn.tockey.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// socket 消息对象 (自定义约定)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SocketMessageVo {
    private String sender_id; // 发送者ID
    private String receiver_id; // 接收者ID
    private String message; // 消息内容
    private Integer type = 0; // 消息类型：0用户消息 1群发消息 2系统消息 3在线用户数据变化
}

