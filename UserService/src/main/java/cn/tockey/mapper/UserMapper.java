package cn.tockey.mapper;

import cn.tockey.domain.User;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;


@Mapper
public interface UserMapper extends BaseMapper<User> {
    // 调用存储过程 注册
    @Select("call insertNewUserProcedure(#{username}, #{password}, #{avatar}, #{email})")
    void insertNewUserProcedure(String username, String password, String avatar, String email);

    // 根据角色id获取用户列表 且分页+条件构造器
    @Select("SELECT users.*, user_role.rid from users \n" +
            "LEFT JOIN user_role ON users.id=user_role.uid ${ew.customSqlSegment} GROUP BY users.username")
    Page<User> getUserByRid(Page<User> page, @Param(Constants.WRAPPER)Wrapper<User> queryWrapper); // 返回值会被mybatis-plus封装到page对象中
}
