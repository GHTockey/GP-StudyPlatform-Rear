package cn.tockey.service.impl;

import cn.tockey.domain.Vocabulary;
import cn.tockey.domain.Words;
import cn.tockey.mapper.VocabularyMapper;
import cn.tockey.mapper.WordsMapper;
import cn.tockey.service.WordsService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tockey
 * @since 2023-12-28
 */
@Service
public class WordsServiceImpl extends ServiceImpl<WordsMapper, Words> implements WordsService {
    @Resource
    private WordsMapper wordsMapper;
    @Resource
    private VocabularyMapper vocabularyMapper;

    //新增新的词语 serviceImpl
    @Override
    public String generateNewWord(Words words) {
        int i = wordsMapper.insert(words);
        return words.getId();
    }

    // 删除词语 serviceImpl
    @Override
    public Integer delWordsById(String id) {
        // 更新所属词集的词条数量
        Words words = wordsMapper.selectOne(new QueryWrapper<Words>().eq("id", id));
        Vocabulary vocabulary = vocabularyMapper.selectById(words.getVId());
        vocabulary.setCount(vocabulary.getCount()-1);
        vocabularyMapper.updateById(vocabulary);

        int i = wordsMapper.deleteById(id);
        return i;
    }

}
