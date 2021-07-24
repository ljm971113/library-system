package com.boot.libsys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
public class TblBookReserves implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 项目编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 图书编号
     */
    private Integer bid;

    /**
     * 图书总量
     */
    private Integer total;

    /**
     * 借出数量
     */
    private Integer borrowed;

    /**
     * 记录状态
     */
    @TableLogic
    private Integer status;
}
