package com.boot.libsys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boot.libsys.entity.TblBorrowinfo;
import org.apache.ibatis.annotations.Select;
import java.util.List;

/**
 * @author Lucky
 * @since 2020-06-03
 */

public interface TblBorrowinfoMapper extends BaseMapper<TblBorrowinfo> {
    //获得借书记录
    @Select("SELECT *, bname FROM tbl_borrowinfo a, tbl_book b WHERE userid = #{uid} AND bookid = bid AND a.status = 1 AND b.status = 1")
    List<TblBorrowinfo> getBookborrowinfo(Integer uid);
}
