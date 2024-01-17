package cn.tockey.mapper;

import cn.tockey.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    // 调用存储过程 注册
    @Select("call insertNewUserProcedure(#{username}, #{password}, #{avatar}, #{email})")
    void insertNewUserProcedure(String username, String password, String avatar, String email);
}
