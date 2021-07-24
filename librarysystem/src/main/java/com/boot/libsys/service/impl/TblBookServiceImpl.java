package com.boot.libsys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.libsys.entity.BookTypeInfo;
import com.boot.libsys.entity.TblBook;
import com.boot.libsys.entity.TblBookReserves;
import com.boot.libsys.entity.TblBookType;
import com.boot.libsys.mapper.TblBookMapper;
import com.boot.libsys.mapper.TblBookReservesMapper;
import com.boot.libsys.service.ITblBookService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lucky
 * @since 2020-06-03
 */

@Service
public class TblBookServiceImpl extends ServiceImpl<TblBookMapper, TblBook> implements ITblBookService {

    @Resource
    private TblBookTypeServiceImpl tblBookTypeService;

    @Resource
    private TblBookReservesMapper tblBookReservesMapper;

    @Resource
    private TblBookMapper tblBookMapper;

    @Override
    public TblBook getById(Serializable id) {
        //设置类型信息
        TblBook book = super.getById(id);
        TblBookType type = tblBookTypeService.getById(book.getTypeid());
        book.setType(type);
        //设置借阅信息
        TblBookReserves reserves = tblBookReservesMapper.selectById(book.getBid());
        book.setTblBookReserves(reserves);
        return book;
    }

    // 统计前10的热度文章信息
    @Override
    public List<TblBook> getHeatBooks(Integer count) {
        List<TblBookReserves> list = tblBookReservesMapper.getBookReserves(count);
        List<TblBook> booklist=new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            TblBook book = this.getById(list.get(i).getBid());
            booklist.add(book);
            //如果当前列表中文章数量已经有 10 个了, 就跳出循环
            if(i >= (count-1)){
                break;
            }
        }
        return booklist;
    }

    @Override
    public PageInfo<TblBook> getBooksByPage(Integer page, Integer size) {
        //设置分页信息, 相当于 limit 后面的参数
        PageHelper.startPage(page, size);
        List<TblBook> bookList = tblBookMapper.selectBookWithPage();
        // 封装图书类型数据
        for (TblBook book : bookList) {
            //设置类型
            TblBookType type = tblBookTypeService.getById(book.getTypeid());
            book.setType(type);
            //设置借阅信息
            TblBookReserves reserves = tblBookReservesMapper.selectById(book.getBid());
            book.setTblBookReserves(reserves);
        }
        return new PageInfo<>(bookList);
    }

    @Override
    public List<BookTypeInfo> getBookTypeInfo() {
        return tblBookMapper.getBookTypeInfo();
    }

    public List<TblBook> setBookAllColumn(List<TblBook> bookList){
        for (int i = 0; i < bookList.size(); ++i) {
            bookList.set(i, this.getById(bookList.get(i).getBid()));
        }
        return bookList;
    }
}
