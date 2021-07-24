package com.boot.libsys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boot.libsys.entity.BookTypeInfo;
import com.boot.libsys.entity.TblBook;
import com.github.pagehelper.PageInfo;
import java.util.List;

/**
 * @author Lucky
 * @since 2020-06-03
 */

public interface ITblBookService extends IService<TblBook> {
    // 统计前 10 的热度文章信息
    List<TblBook> getHeatBooks(Integer count);

    //分页获取图书信息
    PageInfo<TblBook> getBooksByPage(Integer page, Integer size);

    //统计当前图书馆的图书类别以及信息问题
    List<BookTypeInfo> getBookTypeInfo();
}
