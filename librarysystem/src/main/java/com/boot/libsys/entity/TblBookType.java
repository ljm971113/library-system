package com.boot.libsys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class TblBookType implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 图书类别ID
     */
    @TableId(value = "tid", type = IdType.AUTO)
    private Integer tid;

    /**
     * 图书类别名字
     */
    private String typename;


}
