package com.boot.libsys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 * 
 * </p>
 *
 * @author Lucky
 * @since 2020-06-03
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = false)
public class TblUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableId(value = "uid", type = IdType.AUTO)
    private Integer uid;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户性别
     */
    private Integer gender;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户电话
     */
    private String phone;

    /**
     * 用户登录密码
     */
    private String password;

    /**
     * 用户权限
     */
    private String authority;

    /**
     * 用户注册日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate since;

    /**
     * 用户状态
     */
    @TableLogic
    private Integer status;

}
