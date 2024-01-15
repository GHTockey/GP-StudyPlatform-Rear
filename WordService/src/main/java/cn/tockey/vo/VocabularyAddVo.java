package cn.tockey.vo;

import lombok.Data;

import java.util.List;

@Data
public class VocabularyAddVo {
    private String title; // 词集标题
    private String desc; // 词集描述
    private String authorId; // 作者id
    private String cover; // 词集封面

    private List<WordVo> wordsList; // 词集中的单词
}
