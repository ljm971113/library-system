package com.boot.libsys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boot.libsys.entity.TblBookReserves;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import java.util.List;

/**
 * @author Lucky
 * @since 2020-06-03
 */
public interface TblBookReservesMapper extends BaseMapper<TblBookReserves> {
    // 统计图书热度信息
    @Select("SELECT * FROM tbl_book_reserves WHERE status=1 ORDER BY borrowed DESC LIMIT 0, #{count}")
    List<TblBookReserves> getBookReserves(int count);

    //查询图书借阅情况
    @Select("SELECT * FROM tbl_book_reserves WHERE bid = #{bid} AND status = 1")
    TblBookReserves selectByBid(Integer bid);

    //查询当前总图书数量
    @Select("SELECT SUM(total) FROM tbl_book_reserves WHERE status = 1")
    Long getTotalCount();

    //查询图书总借出数量
    @Select("SELECT SUM(borrowed) FROM tbl_book_reserves WHERE status = 1")
    Long getBookBorrowed();

    //新增书籍的数量
    @Update("UPDATE tbl_book_reserves SET total = #{total} WHERE bid = #{bid}")
    int addBookCount(Integer bid, Integer total);

    //减少书籍的数量
    @Update("UPDATE tbl_book_reserves SET total = #{count} WHERE bid = #{bid}")
    int delBookCount(Integer bid, Integer count);

    //批量减少书籍的数量就需要复用上面的方法

}
