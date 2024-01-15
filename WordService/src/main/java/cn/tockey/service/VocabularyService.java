package cn.tockey.service;

import cn.tockey.domain.User;
import cn.tockey.domain.Vocabulary;
import cn.tockey.vo.VocabularyAddVo;
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
    Integer addVocabulary(VocabularyAddVo vocabularyAddVo);
    // 修改词集
    boolean updVocabulary(Vocabulary vocabulary);
    // 删除词集
    boolean delVocabulary(Integer id);
    // 获取词集
    Vocabulary getVocabulary(Integer id);
    // 获取用户的词集列表
    List<Vocabulary> getUserVocabularyListByUid(String uid);
}
