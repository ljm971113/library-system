package com.boot.libsys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boot.libsys.entity.TblBookType;
import org.apache.ibatis.annotations.Select;

/**
 * @author Lucky
 * @since 2020-06-03
 */

public interface TblBookTypeMapper extends BaseMapper<TblBookType> {
    //查询图书种类
    @Select("SELECT COUNT(*) FROM tbl_book_type")
    Long getBookTypes();
}
