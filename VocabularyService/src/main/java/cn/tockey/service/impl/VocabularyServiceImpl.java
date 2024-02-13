package cn.tockey.service.impl;

import cn.tockey.domain.User;
import cn.tockey.domain.UserVocabulary;
import cn.tockey.domain.Vocabulary;
import cn.tockey.domain.Words;
import cn.tockey.feign.UserFeign;
import cn.tockey.mapper.UserVocabularyMapper;
import cn.tockey.mapper.VocabularyMapper;
import cn.tockey.mapper.WordsMapper;
import cn.tockey.service.VocabularyService;
import cn.tockey.utils.JwtProperties;
import cn.tockey.utils.JwtUtils;
import cn.tockey.utils.RsaUtils;
import cn.tockey.vo.BaseResult;
import cn.tockey.vo.VocabularyAddVo;
import cn.tockey.vo.WordVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
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
    @Resource
    private JwtProperties jwtProperties;
    @Resource
    private HttpServletRequest httpServletRequest;
    @Resource
    private UserFeign userFeign;
    @Resource
    private UserVocabularyMapper userVocabularyMapper;



    // jwt 解析用户信息
    public User parseUserByToken(String token) throws Exception {
        User user = JwtUtils.getObjectFromToken(token, jwtProperties.getPublicKey(), User.class);
        System.out.println("token解析；当前用户："+user);
        return user;
    }
    // 关联 程序
    private void relevanceHandler(Vocabulary vocabulary, Boolean isRelevanceWord) {
            // 关联作者
            User author = userFeign.getUserInfoById(vocabulary.getAuthorId()).getData();
            vocabulary.setAuthor(author);
            // 关联学习词集的用户列表
            List<UserVocabulary> userVoc = userVocabularyMapper.selectList(new QueryWrapper<UserVocabulary>().eq("vid", vocabulary.getId()));
            for (UserVocabulary userVocabulary : userVoc) {
                User user = userFeign.getUserInfoById(userVocabulary.getUid()).getData();
                vocabulary.getUserList().add(user);
            }
            // 按需关联词语列表
            if (isRelevanceWord) {
                // 关联词语列表
                List<Words> wordsList = wordsMapper.selectList(new QueryWrapper<Words>().eq("v_id", vocabulary.getId()));
                vocabulary.setWordsList(wordsList);
            }
    }



    // 添加词集 serviceImpl
    @Override
    public Integer addVocabulary(Vocabulary vocabulary) {
        try {
            User currentUser = parseUserByToken(httpServletRequest.getHeader("Authorization"));
            // 拼接数据
            vocabulary.setCount(vocabulary.getWordsList().size()); // 数量
            vocabulary.setCreateTime(new Date()); // 创建时间
            int insertResult = vocabularyMapper.insert(vocabulary);
            // 添加词条
            for (Words words : vocabulary.getWordsList()) {
                Words newWords = new Words(0, vocabulary.getId(), words.getWord(), words.getDefinition());
                wordsMapper.insert(newWords);
            }
            return vocabulary.getId();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    // 修改词集 serviceImpl
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
        return userVocabularyMapper.insert(userVocabulary);
    }
}
