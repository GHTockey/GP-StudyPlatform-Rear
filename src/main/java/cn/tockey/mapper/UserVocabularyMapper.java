package cn.tockey.mapper;

import cn.tockey.domain.UserVocabulary;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserVocabularyMapper extends BaseMapper<UserVocabulary> {
    // 获取学习数最多的用户列表
    @Select("SELECT uv.uid,uv.vid, sum(study_total) study_total FROM user_vocabulary uv GROUP BY uid ORDER BY study_total desc;")
    public List<UserVocabulary> getMostStudyVocList();
}
