package cn.tockey.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author tockey
 * @since 2024-03-07
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@TableName("user_message")
public class UserMessage {

    /**
     * 标识符
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 发送者ID
     */
    private String senderId;

    /**
     * 接受者ID
     */
    private String receiverId;

    /**
     * 消息内容
     */
    private String message;

    /**
     * 消息发送的时间
     */
    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
    private Date timestamp;

    /**
     * 是否已读
     */
    private Integer isRead;

    //消息类型: (0用户消息 1群发消息 2系统消息 3在线用户数据变化  4发送的消息已读)
    private Integer type;
}
