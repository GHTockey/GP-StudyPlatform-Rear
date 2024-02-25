package cn.tockey.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// 词集表
@Data
@TableName("vocabulary")
@NoArgsConstructor
public class Vocabulary {
    @TableId(value = "id", type = IdType.AUTO)
    private String id;
    private String title; // 标题
    @TableField("`desc`")
    private String desc; // 描述
    private String cover; // 封面
    private Integer count; // 词条数量
    private Integer stuNum; // 学习人数
    private String authorId; // 作者id
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime; // 创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime; // 更新时间

    // 关联
    @TableField(exist = false)
    private List<Words> wordsList = new ArrayList<>(); // 词语列表
    @TableField(exist = false)
    private User author; // 作者
    @TableField(exist = false)
    private List<User> userList = new ArrayList<>(); // 学习的用户列表
}