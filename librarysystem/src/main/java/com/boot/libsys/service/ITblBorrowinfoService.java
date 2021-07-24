package com.boot.libsys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boot.libsys.entity.TblBorrowinfo;

/**
 * @author Lucky
 * @since 2020-06-03
 */

public interface ITblBorrowinfoService extends IService<TblBorrowinfo> {
    //借书操作
    int BorrowBook(Integer bid, Integer uid, Integer bookNum);

    //还书操作
    int ReturnBook(Integer bid, Integer uid, Integer bookNum);
}
