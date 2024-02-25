package cn.tockey.mapper;

import cn.tockey.domain.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author tockey
 * @since 2024-01-16
 */
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {
    // 根据角色id获取权限 mapper
    @Select("SELECT distinct permission.* FROM user_role userRole\n" +
            "JOIN role_permission rolePerm ON userRole.rid=rolePerm.rid\n" +
            "JOIN permission ON rolePerm.pid = permission.id\n" +
            "WHERE userRole.uid = #{uid} and permission.type = 'page'")
    List<Permission> getPermissionByUid(String uid);
}
