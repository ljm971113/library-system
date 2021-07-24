package com.boot.libsys.entity;

import lombok.Data;
import lombok.ToString;

/**
 * @author Lucky
 * @version 1.0
 * @since 2020/06/07 14:29
 * 辅助类信息, 用于封装页面展示的信息
 */
@Data
@ToString
public class BookTypeInfo {

    private Integer typeid;
    private String typename;
    private Integer count;

}
