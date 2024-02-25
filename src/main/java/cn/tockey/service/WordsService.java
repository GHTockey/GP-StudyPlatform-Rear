package cn.tockey.service;

import cn.tockey.domain.Words;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tockey
 * @since 2023-12-28
 */
public interface WordsService extends IService<Words> {
    // 新增新的词语
    String generateNewWord(Words words);
    // 删除词语
    Integer delWordsById(String id);
}
