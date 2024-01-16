package cn.tockey.service.impl;

import cn.tockey.domain.Vocabulary;
import cn.tockey.domain.Words;
import cn.tockey.mapper.VocabularyMapper;
import cn.tockey.mapper.WordsMapper;
import cn.tockey.service.VocabularyService;
import cn.tockey.vo.VocabularyAddVo;
import cn.tockey.vo.WordVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author tockey
 * @since 2023-12-28
 */
@Service
@Transactional
public class VocabularyServiceImpl extends ServiceImpl<VocabularyMapper, Vocabulary> implements VocabularyService {
    @Resource
    private VocabularyMapper vocabularyMapper;
    @Resource
    private WordsMapper wordsMapper;

    @Override
    public Integer addVocabulary(VocabularyAddVo vocabularyAddVo) {
        Vocabulary vocabulary = new Vocabulary(
                null,
                vocabularyAddVo.getTitle(),
                vocabularyAddVo.getDesc(),
                vocabularyAddVo.getCover(),
                vocabularyAddVo.getWordsList().size(),
                vocabularyAddVo.getAuthorId(),
                new Date(),
                null,
                null
        );
        int insertResult = vocabularyMapper.insert(vocabulary);
        for (WordVo words : vocabularyAddVo.getWordsList()) {
            Words newWords = new Words(0, vocabulary.getId(), words.getWord(), words.getDefinition());
            wordsMapper.insert(newWords);
        }
        return vocabulary.getId();
    }

    @Override
    public boolean updVocabulary(Vocabulary vocabulary) {
        QueryWrapper<Vocabulary> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", vocabulary.getId());
        vocabulary.setUpdateTime(new Date());
        int updateResult = vocabularyMapper.update(vocabulary, queryWrapper);

        // 更新词条
        if (!vocabulary.getWordsList().isEmpty()) {
            for (Words words : vocabulary.getWordsList()) {
                QueryWrapper<Words> wordsQueryWrapper = new QueryWrapper<>();
                wordsQueryWrapper.eq("id", words.getId());
                wordsQueryWrapper.eq("v_id", vocabulary.getId());
                wordsMapper.update(words, wordsQueryWrapper);
            }
        }
        return updateResult == 1;
    }

    @Override
    public boolean delVocabulary(Integer id) {
        wordsMapper.delete(new QueryWrapper<Words>().eq("v_id", id));
        int vocDelResult = vocabularyMapper.deleteById(id);
        return vocDelResult == 1;
    }

    @Override
    public Vocabulary getVocabulary(Integer id) {
        Vocabulary vocabulary = vocabularyMapper.selectById(id);
        if(vocabulary!=null){
            List<Words> wordsList = wordsMapper.selectList(new QueryWrapper<Words>().eq("v_id", id));
            vocabulary.setWordsList(wordsList);
        }
        return vocabulary;
    }

    @Override
    public List<Vocabulary> getUserVocabularyListByUid(String uid) {
        List<Vocabulary> vocabularyList = vocabularyMapper.selectList(new QueryWrapper<Vocabulary>().eq("author_id", uid));
        return vocabularyList;
    }
}
