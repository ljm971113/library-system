package com.boot.libsys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boot.libsys.entity.TblUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import java.util.List;

/**
 * @author Lucky
 * @since 2020-06-03
 */
public interface TblUserMapper extends BaseMapper<TblUser> {
    //根据姓名查询记录是否存在该用户名
    @Select("SELECT count(*) from tbl_user WHERE username = #{username}")
    int checkUsernameExist(String username);

    //查询管理员个数
    @Select("SELECT COUNT(*) FROM tbl_user WHERE authority = 'ROLE_admin' AND status = 1")
    Long getAdminCount();

    //查询普通用户个数
    @Select("SELECT COUNT(*) FROM tbl_user WHERE authority = 'ROLE_common' AND status = 1")
    Long getCommonCount();

    //查询管理员信息
    @Select("SELECT * FROM tbl_user WHERE authority = 'ROLE_admin'")
    List<TblUser> getAdmins();

    @Select({
            "<script>",
            "SELECT * FROM tbl_user WHERE uid in",
                "<foreach collection='ids' item='id' open='(' separator=',' close=')' >",
                "#{id}",
                "</foreach>",
            "</script>"
    })
    List<TblUser> getUser(@Param("ids")Integer[] ids);

    //禁用管理员
    @Update("UPDATE tbl_user SET status = #{statu} WHERE uid = #{uid}")
    int DelAdmin(Integer statu, Integer uid);

    //获取所有 读者列表
    //查询管理员信息
    @Select("SELECT * FROM tbl_user WHERE authority = 'ROLE_common' AND status = 1")
    List<TblUser> getCommons();
}
