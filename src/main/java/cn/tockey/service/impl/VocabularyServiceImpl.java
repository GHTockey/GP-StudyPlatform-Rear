package cn.tockey.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.tockey.domain.User;
import cn.tockey.domain.UserVocabulary;
import cn.tockey.domain.Vocabulary;
import cn.tockey.domain.Words;
import cn.tockey.mapper.UserVocabularyMapper;
import cn.tockey.mapper.VocabularyMapper;
import cn.tockey.mapper.WordsMapper;
import cn.tockey.service.UserService;
import cn.tockey.service.VocabularyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
public class VocabularyServiceImpl extends ServiceImpl<VocabularyMapper, Vocabulary> implements VocabularyService {
    @Resource
    private VocabularyMapper vocabularyMapper;
    @Resource
    private WordsMapper wordsMapper;
    @Resource
    private UserService userService;
    @Resource
    private UserVocabularyMapper userVocabularyMapper;

    // jwt 解析用户信息
    public String parseUserIdByToken() {
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        System.out.println("token解析；当前用户："+ tokenInfo.getLoginId());
        return (String) tokenInfo.getLoginId();
    }
    // 词集的关联程序
    private void relevanceHandler(Vocabulary vocabulary, Boolean isRelevanceWord) {
            // 关联作者
            User author = userService.getUserInfoById(vocabulary.getAuthorId());
            vocabulary.setAuthor(author);
            // 关联学习词集的用户列表
            List<UserVocabulary> userVoc = userVocabularyMapper.selectList(new QueryWrapper<UserVocabulary>().eq("vid", vocabulary.getId()));
            for (UserVocabulary userVocabulary : userVoc) {
                User user = userService.getUserInfoById(userVocabulary.getUid());
                vocabulary.getUserList().add(user);
            }
            // 按需关联词语列表
            if (isRelevanceWord) {
                // 关联词语列表
                List<Words> wordsList = wordsMapper.selectList(new QueryWrapper<Words>().eq("v_id", vocabulary.getId()));
                vocabulary.setWordsList(wordsList);
                // 词条数量
                vocabulary.setCount(wordsList.size());
            }
    }



    // 添加词集 serviceImpl
    @Override
    public String addVocabulary(Vocabulary vocabulary) {
        try {
            // 拼接数据
            vocabulary.setId(null);
            vocabulary.setAuthorId(parseUserIdByToken());
            vocabulary.setCount(vocabulary.getWordsList().size()); // 数量
            vocabulary.setCreateTime(new Date()); // 创建时间
            int insertResult = vocabularyMapper.insert(vocabulary);
            // 添加词条
            for (Words words : vocabulary.getWordsList()) {
                Words newWords = new Words(null, vocabulary.getId(), words.getWord(), words.getDefinition());
                wordsMapper.insert(newWords);
            }
            return vocabulary.getId();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 修改词集 serviceImpl
    @Override
    public boolean updVocabulary(Vocabulary vocabulary) {
        QueryWrapper<Vocabulary> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", vocabulary.getId());
        vocabulary.setUpdateTime(new Date()); // 设置更新时间
        int updateResult = vocabularyMapper.update(vocabulary, queryWrapper);

        // 更新词条
        if (!vocabulary.getWordsList().isEmpty()) {
            for (Words words : vocabulary.getWordsList()) {
                QueryWrapper<Words> wordsQueryWrapper = new QueryWrapper<>();
                wordsQueryWrapper.eq("id", words.getId());
                wordsQueryWrapper.eq("v_id", vocabulary.getId());
                if (words.getId() == null) {
                    wordsMapper.insert(words);
                } else {
                    wordsMapper.update(words, wordsQueryWrapper);
                }
            }
            // 更新词条数量
            vocabulary.setCount(vocabulary.getWordsList().size());
        }
        return updateResult == 1;
    }

    // 删除词集 serviceImpl
    @Override
    public boolean delVocabulary(Integer id) {
        wordsMapper.delete(new QueryWrapper<Words>().eq("v_id", id));
        int vocDelResult = vocabularyMapper.deleteById(id);
        return vocDelResult == 1;
    }

    // 获取词集 serviceImpl
    @Override
    public Vocabulary getVocabularyById(Integer id) {
        Vocabulary vocabulary = vocabularyMapper.selectById(id);
        if(vocabulary!=null){
            // 关联词语列表
            List<Words> wordsList = wordsMapper.selectList(new QueryWrapper<Words>().eq("v_id", vocabulary.getId()));
            vocabulary.setWordsList(wordsList);
            // 关联程序
            relevanceHandler(vocabulary,true);
        }
        return vocabulary;
    }

    // 获取用户的词集列表 serviceImpl
    @Override
    public List<Vocabulary> getUserVocabularyListByUid(String uid) {
        List<Vocabulary> vocabularyList = vocabularyMapper.selectList(new QueryWrapper<Vocabulary>().eq("author_id", uid));
        for (Vocabulary vocabulary : vocabularyList) {
            relevanceHandler(vocabulary, false);
        }
        return vocabularyList;
    }

    // 获取词集列表 serviceImpl
    @Override
    public List<Vocabulary> getVocabularyList() {
        List<Vocabulary> vocabularyList = vocabularyMapper.selectList(null);
        for (Vocabulary vocabulary : vocabularyList) {
            relevanceHandler(vocabulary, false);
        }
        return vocabularyList;
    }

    // 搜索词集 serviceImpl
    @Override
    public List<Vocabulary> searchVocabulary(String keyword) {
        List<Vocabulary> list = vocabularyMapper.selectList(new QueryWrapper<Vocabulary>().like("title", keyword));
        for (Vocabulary vocabulary : list) {
            relevanceHandler(vocabulary, false);
        }
        return list;
    }

    // 用户学习词集 【关联表】 serviceImpl
    @Override
    public Integer userRelevanceVoc(UserVocabulary userVocabulary) {
        Vocabulary vocabulary = vocabularyMapper.selectOne(new QueryWrapper<Vocabulary>().eq("id", userVocabulary.getVid()));
        vocabulary.setStuNum(vocabulary.getStuNum()+1);
        vocabularyMapper.updateById(vocabulary);
        return userVocabularyMapper.insert(userVocabulary);
    }

    // 用户取消学习词集 【关联表】 serviceImpl
    @Override
    public Integer userCancelRelevanceVoc(UserVocabulary userVocabulary) {
        int i = userVocabularyMapper.delete(new QueryWrapper<UserVocabulary>().eq("uid", userVocabulary.getUid()).eq("vid", userVocabulary.getVid()));
        // 更新词集学习人数
        Vocabulary vocabulary = vocabularyMapper.selectOne(new QueryWrapper<Vocabulary>().eq("id", userVocabulary.getVid()));
        vocabulary.setStuNum(vocabulary.getStuNum()-1);
        vocabularyMapper.updateById(vocabulary);
        return i;
    }

    // 获取用户学习的词集列表 serviceImpl
    @Override
    public List<Vocabulary> getUserRelevanceVocListByUid(String uid) {
        List<UserVocabulary> userVocabularies = userVocabularyMapper.selectList(new QueryWrapper<UserVocabulary>().eq("uid", uid));
        ArrayList<Vocabulary> list = new ArrayList<>();
        for (UserVocabulary userVocabulary : userVocabularies) {
            Vocabulary vocabulary = vocabularyMapper.selectOne(new QueryWrapper<Vocabulary>().eq("id", userVocabulary.getVid()));
            relevanceHandler(vocabulary, false);
            list.add(vocabulary);
        }
        return list;
    }

    // 获取学习数最多的词集列表 serviceImpl
    @Override
    public List<Vocabulary> getMostStudyVocList() {
        List<Vocabulary> list = vocabularyMapper.selectList(new QueryWrapper<Vocabulary>().orderByDesc("stu_num"));
        for (Vocabulary vocabulary : list) {
            relevanceHandler(vocabulary, false);
        }
        return list;
    }

    // 更新词集学习数 serviceImpl
    @Override
    public Integer updateVocStudyTotal(String uid, String vid) {
        UserVocabulary userVocabulary = userVocabularyMapper.selectOne(new QueryWrapper<UserVocabulary>().eq("uid", uid).eq("vid", vid));
        userVocabulary.setStudyTotal(userVocabulary.getStudyTotal()+1);
        int updated = userVocabularyMapper.update(userVocabulary, new QueryWrapper<UserVocabulary>().eq("uid", uid).eq("vid", vid));
        return updated;
    }
}
