package com.boot.libsys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boot.libsys.entity.BookTypeInfo;
import com.boot.libsys.entity.TblBook;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Lucky
 * @since 2020-06-03
 */
public interface TblBookMapper extends BaseMapper<TblBook> {
    // 图书分页查询
    @Select("SELECT * FROM tbl_book ORDER BY bid AND status = 1")
    List<TblBook> selectBookWithPage();

    @Select("select typeid, typename, count(*) count from tbl_book b,tbl_book_type t  where b.typeid = t.tid group by typeid")
    List<BookTypeInfo> getBookTypeInfo();

    //查询当前图书馆图书的数量, (一种一本)
    @Select("SELECT COUNT(*) FROM tbl_book")
    long getBookCount();
}
