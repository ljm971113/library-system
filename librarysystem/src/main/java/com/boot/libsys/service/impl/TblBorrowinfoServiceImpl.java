package com.boot.libsys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.libsys.entity.TblBookReserves;
import com.boot.libsys.entity.TblBorrowinfo;
import com.boot.libsys.mapper.TblBookReservesMapper;
import com.boot.libsys.mapper.TblBorrowinfoMapper;
import com.boot.libsys.service.ITblBorrowinfoService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Lucky
 * @since 2020-06-03
 */

@Service
public class TblBorrowinfoServiceImpl extends ServiceImpl<TblBorrowinfoMapper, TblBorrowinfo> implements ITblBorrowinfoService {

    @Resource
    private TblBookReservesMapper tblBookReservesMapper;
    @Resource
    private TblBorrowinfoMapper tblBorrowinfoMapper;

    @Override
    public int BorrowBook(Integer bid, Integer uid, Integer bookNum) {
        //借书操作需要的步骤:
        //1. 如果有记录, 将书的数量和原数据进行相加, 并且更新借书时间
        //2. 更新图书借阅表的信息
        List<TblBorrowinfo> info = getBorrowInfoList(bid, uid);
        if(info.size() > 0){
            //存在记录, 将更新他
            TblBorrowinfo borrowinfo = info.get(0);
            borrowinfo.setTotalbrrow(borrowinfo.getTotalbrrow()+bookNum);
            LocalDate date = LocalDate.now();
            borrowinfo.setBrrodate(date);
            borrowinfo.setReturndate(date.plusMonths(1));
            int i = tblBorrowinfoMapper.updateById(borrowinfo);

            //将借阅情况同步到借阅表中
            TblBookReserves reserves = tblBookReservesMapper.selectByBid(bid);
            reserves.setBorrowed(reserves.getBorrowed()+bookNum);
            int j = tblBookReservesMapper.updateById(reserves);
            return i+j;
        }else{
            //不存在该条借书记录
            TblBorrowinfo borrowinfo = new TblBorrowinfo();
            borrowinfo.setUserid(uid);
            borrowinfo.setBookid(bid);
            borrowinfo.setTotalbrrow(bookNum);
            LocalDate date = LocalDate.now();
            borrowinfo.setBrrodate(date);
            borrowinfo.setReturndate(date.plusMonths(1));
            int i = tblBorrowinfoMapper.insert(borrowinfo);

            //将借阅情况同步到借阅表中
            TblBookReserves reserves = tblBookReservesMapper.selectByBid(bid);
            reserves.setBorrowed(reserves.getBorrowed()+bookNum);
            int j = tblBookReservesMapper.updateById(reserves);
            return i+j;
        }
    }

    @Override
    public int ReturnBook(Integer bid, Integer uid, Integer bookNum) {
        //还书操作需要的步骤:
        //1. 如果有记录, 将书的数量和原数据进行相减并分析结果,如果数据为0,则删除该记录,如果不为零,更新数据
        //2. 更新图书借阅表的信息
        Map<String, Object> map = new HashMap<>();
        map.put("userid", uid);
        map.put("bookid", bid);
        List<TblBorrowinfo> info = getBorrowInfoList(bid, uid);
        if(info.size() > 0){
            //存在记录, 获取记录, 判断借书总数
            TblBorrowinfo borrowinfo = info.get(0);
            System.out.println("当前已借 " + borrowinfo.getTotalbrrow() + " 本书!");
            System.out.println("需要归还 " + bookNum + " 本书!");
            if((borrowinfo.getTotalbrrow() - bookNum) == 0){
                //代表全部归还,则删除该记录(实际上会进行逻辑删除)
                int i = tblBorrowinfoMapper.deleteByMap(map);
                //同步更新借阅表
                TblBookReserves reserves = tblBookReservesMapper.selectByBid(bid);
                reserves.setBorrowed(0);
                int j = tblBookReservesMapper.updateById(reserves);
                return i+j;
            }
            //否则就是归还部分图书
            borrowinfo.setTotalbrrow(borrowinfo.getTotalbrrow() - bookNum);
            int i = tblBorrowinfoMapper.updateById(borrowinfo);

            //将借阅情况同步到借阅表中
            TblBookReserves reserves = tblBookReservesMapper.selectByBid(bid);
            reserves.setBorrowed(reserves.getBorrowed() - bookNum);
            int j = tblBookReservesMapper.updateById(reserves);
            return i+j;
        }
        return 0;
    }

    private List<TblBorrowinfo> getBorrowInfoList(Integer bid, Integer uid){
        Map<String, Object> map = new HashMap<>();
        map.put("userid", uid);
        map.put("bookid", bid);
        return tblBorrowinfoMapper.selectByMap(map);
    }
}
