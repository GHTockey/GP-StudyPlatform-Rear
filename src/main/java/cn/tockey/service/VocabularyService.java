package cn.tockey.service;

import cn.tockey.domain.UserVocabulary;
import cn.tockey.domain.Vocabulary;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tockey
 * @since 2023-12-28
 */
public interface VocabularyService extends IService<Vocabulary> {
    // 添加词集
    String addVocabulary(Vocabulary vocabulary);
    // 修改词集
    boolean updVocabulary(Vocabulary vocabulary);
    // 删除词集
    boolean delVocabulary(Integer id);
    // 获取词集
    Vocabulary getVocabularyById(Integer id);
    // 获取用户的词集列表
    List<Vocabulary> getUserVocabularyListByUid(String uid);
    // 获取词集列表
    List<Vocabulary> getVocabularyList();
    // 搜索词集
    List<Vocabulary> searchVocabulary(String keyword);
    // 用户学习词集 【关联表】
    Integer userRelevanceVoc(UserVocabulary userVocabulary);
    // 用户取消学习词集 【关联表】
    Integer userCancelRelevanceVoc(UserVocabulary userVocabulary);
    // 获取用户学习的词集列表
    List<Vocabulary> getUserRelevanceVocListByUid(String uid);
}
