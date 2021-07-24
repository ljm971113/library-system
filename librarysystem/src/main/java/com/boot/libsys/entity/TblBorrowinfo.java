package com.boot.libsys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

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
public class TblBorrowinfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 借阅信息ID
     */
    @TableId(value = "browid", type = IdType.AUTO)
    private Integer browid;

    /**
     * 用户ID
     */
    private Integer userid;

    /**
     * 书籍ID
     */
    private Integer bookid;

    /**
     * 借书日期
     */
    private LocalDate brrodate;

    /**
     * 应还书日期
     */
    private LocalDate returndate;

    /**
     * 记录状态
     */
    @TableLogic //逻辑删除
    private Integer status;

    /**
     * 借书总数
     */
    private Integer totalbrrow;

    @TableField(exist = false)  //重点:Mybatis-plus中默认操作忽略该字段
    private String bname;

}
