package com.boot.libsys.controller.common;

import com.boot.libsys.entity.TblBook;
import com.boot.libsys.entity.TblBorrowinfo;
import com.boot.libsys.entity.TblUser;
import com.boot.libsys.mapper.TblBorrowinfoMapper;
import com.boot.libsys.mapper.TblUserMapper;
import com.boot.libsys.service.impl.TblBookServiceImpl;
import com.boot.libsys.service.impl.TblBorrowinfoServiceImpl;
import com.boot.libsys.service.impl.TblUserServiceImpl;
import com.boot.libsys.utils.Commons;
import com.boot.libsys.utils.Result;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Lucky
 * @version 1.0
 * @since 2020/06/04 13:09
 */
@Controller
@RequestMapping("/common")
@SuppressWarnings("all")
public class CommonController {

    private final TblBookServiceImpl tblBookService;
    private final Commons commons;
    private final TblBorrowinfoMapper tblBorrowinfoMapper;
    private final TblBorrowinfoServiceImpl tblBorrowinfoService;
    private final TblUserServiceImpl tblUserService;
    private final TblUserMapper tblUserMapper;

    @Autowired
    public CommonController(TblBookServiceImpl tblBookService, Commons commons, TblBorrowinfoMapper tblBorrowinfoMapper, TblBorrowinfoServiceImpl tblBorrowinfoService, TblUserServiceImpl tblUserService, TblUserMapper tblUserMapper){
        this.tblBookService = tblBookService;
        this.commons = commons;
        this.tblBorrowinfoMapper = tblBorrowinfoMapper;
        this.tblBorrowinfoService = tblBorrowinfoService;
        this.tblUserMapper = tblUserMapper;
        this.tblUserService = tblUserService;
    }


    @GetMapping(value = {"/index","/"})
    public String index(HttpServletRequest request){
        request.setAttribute("commons", commons);
        /*??????????????????*/
        HttpSession session = request.getSession();
        Result result = (Result) session.getAttribute("result");
        TblUser user = (TblUser)result.getData();
        List<TblBorrowinfo> borroInfoList = tblBorrowinfoMapper.getBookborrowinfo(user.getUid());
        request.setAttribute("borroInfoList",borroInfoList);
        return this.index(request, 1, 5);
    }

    // ?????????
    @GetMapping(value = "/page/{p}")
    public String index(HttpServletRequest request, @PathVariable("p") int page, @RequestParam(value = "count", defaultValue = "5") int count) {
        PageInfo<TblBook> books = tblBookService.getBooksByPage(page, count);
        // ??????????????????????????????
        List<TblBook> bookList = tblBookService.getHeatBooks(10);
        request.setAttribute("books", books);
        request.setAttribute("bookList", bookList);
        request.setAttribute("commons", commons);
        /*??????????????????*/
        HttpSession session = request.getSession();
        Result result = (Result) session.getAttribute("result");

        TblUser user = (TblUser)result.getData();
        List<TblBorrowinfo> borroInfoList = tblBorrowinfoMapper.getBookborrowinfo(user.getUid());
        request.getSession().setAttribute("borroInfoList",borroInfoList);
        return "common/index";
    }

    @GetMapping("/book/{bid}")
    public String bookDetil(HttpServletRequest request,@PathVariable("bid") int bid){
        TblBook book = tblBookService.getById(bid);
        request.setAttribute("book", book);
        return "common/bookDetil";
    }

    //???????????? AJAX ??????
    @GetMapping("/borrowOrreturn")
    @ResponseBody
    public Result borrow(HttpServletRequest request, Integer bid, Integer uid, Integer number, Boolean isBorrow){
        //????????????????????????ID, ??????ID, ???/???????????????
        System.out.println(bid + ":" + uid + ":" + number + ":" + isBorrow);

        if(isBorrow){
            int i = tblBorrowinfoService.BorrowBook(bid, uid, number);
            if(i == 2){
                //??????????????????session
                /*??????????????????*/
                HttpSession session = request.getSession();
                Result reqult = (Result) session.getAttribute("result");
                TblUser user = (TblUser)reqult.getData();
                List<TblBorrowinfo> borroInfoList = tblBorrowinfoMapper.getBookborrowinfo(user.getUid());
                request.getSession().setAttribute("borroInfoList",borroInfoList);
                return Result.success(200, "????????????!", null);
            }else {
                return Result.success(400, "??????, ?????????!", null);
            }
        }else{
            int i = tblBorrowinfoService.ReturnBook(bid, uid, number);
            if(i == 2){
                //??????????????????session
                /*??????????????????*/
                HttpSession session = request.getSession();
                Result reqult = (Result) session.getAttribute("result");
                TblUser user = (TblUser)reqult.getData();
                List<TblBorrowinfo> borroInfoList = tblBorrowinfoMapper.getBookborrowinfo(user.getUid());
                request.getSession().setAttribute("borroInfoList",borroInfoList);
                return Result.success(200, "????????????!", null);
            }else {
                return Result.success(400, "??????, ?????????!", null);
            }
        }
    }

    @GetMapping("/longger")
    @ResponseBody
    public Result longger(Integer bid, Integer uid){
        Map<String, Object> map = new HashMap<>();
        map.put("userid", uid);
        map.put("bookid", bid);
        List<TblBorrowinfo> borrowinfos = tblBorrowinfoMapper.selectByMap(map);
        int i = 0;
        if(borrowinfos.size() > 0){
            LocalDate date = LocalDate.now();
            TblBorrowinfo borrowinfo = borrowinfos.get(0);
            borrowinfo.setBrrodate(date);
            borrowinfo.setReturndate(date.plusMonths(1));
            i = tblBorrowinfoMapper.updateById(borrowinfo);
        }
        if(i > 0){
            return Result.success(200, "????????????, ?????????????????????????????????!", null);
        }else{
            return Result.success(400, "????????????!", null);
        }
    }

    @PostMapping("/modifyInfo")
    public String modifyInfo(TblUser user, HttpSession session){
        //????????????????????????????????? ??????????????????????????????
        System.out.println(user);
        //???????????????
        boolean flag = false;
        //???????????????????????????
        if(user != null && checkUsername(user.getUsername())){
            flag = tblUserService.updateById(user);
        }
        //??????????????????
        if(flag){
            ((Result)session.getAttribute("result")).setData(user);
            return "success";
        }else{
            return "error";
        }
    }

    @GetMapping("/checkUsername")
    @ResponseBody
    public boolean checkUsername(String username){
        return tblUserMapper.checkUsernameExist(username) == 0;
    }

    //??????????????????
    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //????????????????????????session??????
        request.getSession().invalidate();
        request.logout();
        return "redirect:/";
    }
}
