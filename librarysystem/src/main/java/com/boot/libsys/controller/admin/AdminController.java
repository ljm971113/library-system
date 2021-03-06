package com.boot.libsys.controller.admin;

import com.boot.libsys.entity.TblBook;
import com.boot.libsys.entity.TblBookReserves;
import com.boot.libsys.entity.TblBookType;
import com.boot.libsys.entity.TblUser;
import com.boot.libsys.mapper.TblBookReservesMapper;
import com.boot.libsys.mapper.TblBookTypeMapper;
import com.boot.libsys.mapper.TblUserMapper;
import com.boot.libsys.service.impl.TblBookServiceImpl;
import com.boot.libsys.utils.Commons;
import com.boot.libsys.utils.Result;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Lucky
 * @version 1.0
 * @since 2020/06/04 13:09
 */
@Controller
@RequestMapping("/admin")
@SuppressWarnings("all")
public class AdminController {

    private final TblUserMapper tblUserMapper;
    private final Commons commonsUtil;
    private final TblBookServiceImpl tblBookService;
    private final TblBookReservesMapper tblBookReservesMapper;
    private final TblBookTypeMapper tblBookTypeMapper;

    @Autowired
    public AdminController(TblUserMapper tblUserMapper,Commons commonsUtil,TblBookServiceImpl tblBookService,TblBookReservesMapper tblBookReservesMapper,TblBookTypeMapper tblBookTypeMapper){
        this.tblUserMapper = tblUserMapper;
        this.commonsUtil = commonsUtil;
        this.tblBookService = tblBookService;
        this.tblBookTypeMapper = tblBookTypeMapper;
        this.tblBookReservesMapper = tblBookReservesMapper;
    }

    //??????????????????, ??????????????????
    @GetMapping("/userList")
    public String toUserList(HttpServletRequest request){
        //?????????????????????request??????
        List<TblUser> commons = tblUserMapper.getCommons();
        System.out.println("commons:" + commons);
        request.setAttribute("commons", commons);
        return "public/userList";
    }

    //?????? ??????
    @GetMapping("/delCommon")
    @ResponseBody
    public Result delCommon(@RequestParam("ids[]")Integer[] ids, HttpServletRequest request){
        System.out.println(Arrays.toString(ids));
        if(ids != null && ids.length > 0){
            List<TblUser> commons = tblUserMapper.getCommons();
            if(commons != null){
                int i = tblUserMapper.deleteBatchIds(Arrays.asList(ids));
                if(i == ids.length){
                    return Result.success(200, "????????????", null);
                }else{
                    return Result.fail(405, "????????????,???????????????", null);
                }
            }else{
                return Result.fail(404, "??????????????????,?????????", null);
            }

        }
        return Result.fail(404, "????????????", null);
    }

    @GetMapping("/bookList")
    public String bookList(HttpServletRequest request){
        //????????????, ???????????????????????????1???, ????????????10???
        return this.bookList(request, 1, 10);
    }

    @GetMapping("/bookList/page/{page}")
    public String bookList(HttpServletRequest request, @PathVariable("page")int page, @RequestParam(value = "size",defaultValue = "10")int size){
        //???????????????????????????
        PageInfo<TblBook> bookPageInfo = tblBookService.getBooksByPage(page, size);
        request.getSession().setAttribute("bookPageInfo", bookPageInfo);
        //???????????????
        request.getSession().setAttribute("commonsUtil", commonsUtil);
        return "public/bookList";
    }

    @GetMapping("/changeBookCount")
    @ResponseBody
    public Result addBookCount(@RequestParam("info[]")Integer[] info, HttpServletRequest request){
        TblBook book = tblBookService.getById(info[0]);
        int total = 0;
        boolean flag = info[2] == 1;
        int i = 0;
        if(flag){
            total = book.getTblBookReserves().getTotal() + info[1];
            i = tblBookReservesMapper.addBookCount(info[0], total);
        }else{
            total = book.getTblBookReserves().getTotal() - info[1];
            i = tblBookReservesMapper.delBookCount(info[0], total);
        }
        if(i > 0){
            //???????????? request ????????? booklist
            PageInfo<TblBook> bookPageInfo = (PageInfo<TblBook>)request.getSession().getAttribute("bookPageInfo");
            List<TblBook> bookList = bookPageInfo.getList();
            for (int m = 0; m < bookList.size(); ++m) {
                if(bookList.get(m).getBid().equals(book.getBid())){
                    bookList.set(m, book);
                    break;
                }
            }
            request.getSession().setAttribute("bookPageInfo", bookPageInfo);
            return Result.success(200, flag ? "????????????!" : "????????????!", null);
        }else{
            return Result.fail(404, flag ? "????????????!" : "????????????!", null);
        }
    }

    @GetMapping("/delBookCountBatch")
    @ResponseBody
    public Result delBookCountBatch(@RequestParam("ids[]")Integer[] info, HttpServletRequest request){
        System.out.println("??????????????? ID:" + Arrays.toString(info));
        int succ = 0;
        //????????????
        //??????????????? id, ???????????? delBookCount() ????????????
        for (Integer id : info) {
            TblBook book = tblBookService.getById(id);
            //?????????????????????????????????
            succ += tblBookReservesMapper.addBookCount(id, book.getTblBookReserves().getBorrowed());
        }
        if(succ == 0){
            return Result.fail(404, "?????????????????????????????????!", null);
        }else if(succ > 0 && succ < info.length){
            return Result.fail(404, "????????????????????????, ???????????????????????????!", null);
        }else{
            return Result.success(200, "?????????????????????????????????!", null);
        }
    }

    @PostMapping("/addBook")
    public String addBook(TblBook book, Integer number, HttpServletRequest request){
        System.out.println(book + ":" + number);//????????????
        if(book != null && number != null && number > 0){
            int i = tblBookService.save(book) ? 1 : 0;//????????????
            //????????????????????????, tbl_book_re... ???
            TblBookReserves reserves = new TblBookReserves();
            reserves.setBid(book.getBid());
            reserves.setTotal(number);
            reserves.setBorrowed(0);
            int j = tblBookReservesMapper.insert(reserves);
            if((i + j) == 2){
                request.setAttribute("res",Result.success(200, "??????????????????", null));
            }else{
                request.setAttribute("res",Result.fail(404, "??????????????????, ?????????", null));
            }
            //?????????????????????request??????
            List<TblBookType> types = tblBookTypeMapper.selectList(null);
            if(types != null && types.size() > 0){
                request.setAttribute("types", types);
            }
            return "public/addBook";
        }
        request.setAttribute("res",Result.fail(404, "??????????????????, ?????????", null));
        //?????????????????????request??????
        List<TblBookType> types = tblBookTypeMapper.selectList(null);
        if(types != null && types.size() > 0){
            request.setAttribute("types", types);
        }

        return "public/addBook";
    }

    @PostMapping("/searchBook")
    public String searchBook(Integer bid, String bname, String author, String companyname, Integer typeid, HttpServletRequest request){

        System.out.println(bid + ":" + bname + ":" + author + ":" + companyname + ":" + typeid);

        //?????????????????????
        Map<String, Object> searchColum = new HashMap<>();
        if(bid != null){
            searchColum.put("bid", bid);
        }
        if(StringUtil.isNotEmpty(bname)){
            searchColum.put("bname", bname);
        }
        if(StringUtil.isNotEmpty(author)){
            searchColum.put("author", author);
        }
        if(StringUtil.isNotEmpty(companyname)){
            searchColum.put("companyname", companyname);
        }
        if(typeid != null){
            searchColum.put("typeid", typeid);
        }
        List<TblBook> bookList = (List<TblBook>) tblBookService.listByMap(searchColum);
        //?????????????????????bookList???????????????????????????, ????????????????????????
        bookList = tblBookService.setBookAllColumn(bookList);
        //??????????????? ????????????
        //???????????????????????????????????????
        List<TblBookType> types = tblBookTypeMapper.selectList(null);
        if(types != null && types.size() > 0){
            request.setAttribute("types", types);
        }

        if(bookList.size() == 0){
            request.setAttribute("searchResult", null);
            return "public/searchBook";
        }
        request.setAttribute("searchResult", bookList);
        request.setAttribute("commonsUtil", commonsUtil);
        return "public/searchBook";
    }

    @GetMapping("/addType")
    @ResponseBody
    public Result addType(String newTypeName,HttpServletRequest request){
        //?????????????????????
        //???????????????????????????????????????
        if(StringUtils.isNotEmpty(newTypeName)){
            List<TblBookType> types = tblBookTypeMapper.selectList(null);
            for (TblBookType type : types) {
                if(type.getTypename().equals(newTypeName)){
                    return Result.fail(404, "????????????,??????????????????!", null);
                }
            }
            //????????????, ?????????
            TblBookType type = new TblBookType();
            type.setTypename(newTypeName);

            int val = tblBookTypeMapper.insert(type);

            if(val > 0){
                //????????????request??????????????????
                //???????????????????????????????????????
                List<TblBookType> typesnew = tblBookTypeMapper.selectList(null);
                if(typesnew != null && types.size() > 0){
                    request.setAttribute("types", typesnew);
                }
                return Result.success(200, "?????? " + newTypeName + " ????????????!", null);
            }else{
                return Result.fail(404, "?????? " + newTypeName + " ????????????!", null);
            }
        }
        return Result.success(405, "????????????, ?????????!", null);
    }
}