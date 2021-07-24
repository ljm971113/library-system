package com.boot.libsys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

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
public class TblBook implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 图书编号
     */
    @TableId(value = "bid", type = IdType.AUTO)
    private Integer bid;

    /**
     * 图书名称
     */
    private String bname;

    /**
     * 图书类别ID
     */
    private Integer typeid;

    /**
     * 图书作者
     */
    private String author;

    /**
     * 图书出版社名称
     */
    private String companyname;

    /**
     * 图书状态标志
     */
    @TableLogic
    private Integer status;

    /**
     * 图书简介
     */
    private String content;

    @TableField(exist = false)  //重点:Mybatis-plus中默认操作忽略该字段
    private TblBookType type;

    @TableField(exist = false)
    private TblBookReserves tblBookReserves;
}
