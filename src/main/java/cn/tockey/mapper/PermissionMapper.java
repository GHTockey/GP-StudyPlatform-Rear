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
    // 根据uid获取权限列表 mapper (仅type为page)
    @Select("SELECT distinct permission.* FROM user_role userRole\n" +
            "JOIN role_permission rolePerm ON userRole.rid=rolePerm.rid\n" +
            "JOIN permission ON rolePerm.pid = permission.id\n" +
            "WHERE userRole.uid = #{uid} and permission.type = 'page'")
    List<Permission> getPagePermissionListByUid(String uid);
    // 根据uid获取权限列表 mapper (所有)
    @Select("SELECT distinct permission.* FROM user_role userRole\n" +
            "JOIN role_permission rolePerm ON userRole.rid=rolePerm.rid\n" +
            "JOIN permission ON rolePerm.pid = permission.id\n" +
            "WHERE userRole.uid = #{uid}")
    List<Permission> getAllPermissionListByUid(String uid);
}
