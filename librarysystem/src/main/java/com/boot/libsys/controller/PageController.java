package com.boot.libsys.controller;

import com.boot.libsys.entity.BookTypeInfo;
import com.boot.libsys.entity.TblBook;
import com.boot.libsys.entity.TblBookType;
import com.boot.libsys.entity.TblUser;
import com.boot.libsys.mapper.TblBookMapper;
import com.boot.libsys.mapper.TblBookReservesMapper;
import com.boot.libsys.mapper.TblBookTypeMapper;
import com.boot.libsys.mapper.TblUserMapper;
import com.boot.libsys.service.impl.TblBookServiceImpl;
import com.boot.libsys.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Lucky
 * @version 1.0
 * @since 2020/06/03 18:51
 */

@Controller
@SuppressWarnings("all")
public class PageController {

    private final TblBookTypeMapper tblBookTypeMapper;
    private final TblBookReservesMapper tblBookReservesMapper;
    private final TblUserMapper tblUserMapper;
    private final TblBookServiceImpl tblBookService;
    private final TblBookMapper tblBookMapper;

    @Autowired
    public PageController(TblBookTypeMapper tblBookTypeMapper, TblBookReservesMapper tblBookReservesMapper, TblUserMapper tblUserMapper, TblBookServiceImpl tblBookService, TblBookMapper tblBookMapper){
        this.tblBookTypeMapper = tblBookTypeMapper;
        this.tblBookReservesMapper = tblBookReservesMapper;
        this.tblUserMapper = tblUserMapper;
        this.tblBookService = tblBookService;
        this.tblBookMapper = tblBookMapper;
    }


    @GetMapping(value = {"/","/login"})
    public String login(){
        return "login";
    }

    @GetMapping("/error_403")
    public String error_403(){
        return "error_403";
    }


    @GetMapping("/common/toModify")
    public String toModify(HttpServletRequest request){
        StringBuffer url = request.getRequestURL();
        url.append("/../");
        request.setAttribute("preUrl", url);
        return "modifyInfo";
    }

    //??????????????????
    @GetMapping("/toRegister")
    public String toRegister(HttpServletRequest request){
        StringBuffer url = request.getRequestURL();
        url.append("/../");
        request.setAttribute("preUrl", url);
        request.getSession().setAttribute("regFlg", true);//??????????????????
        return "register";
    }

    @PostMapping("/welcome")
    public String toWelcome(HttpServletRequest request){
        //??????????????????????????????????????????
        //1. ?????????????????????
        Long totalCount = tblBookReservesMapper.getTotalCount();
        //2. ????????????????????????
        Long totalBorrowed = tblBookReservesMapper.getBookBorrowed();
        //3. ?????????????????????
        Long adminCount = tblUserMapper.getAdminCount();
        //4. ????????????????????????
        Long commonCount = tblUserMapper.getCommonCount();
        //5. ????????????????????????
        Long bookTypes = tblBookTypeMapper.getBookTypes();
        //6. ?????????4???????????????
        List<TblBook> books = tblBookService.getHeatBooks(4);
        Map<String, Object> info = new HashMap<>(6);
        info.put("totalCount", Objects.isNull(totalCount)?0:totalCount);
        info.put("totalBorrowed", Objects.isNull(totalBorrowed)?0:totalBorrowed);
        info.put("adminCount", Objects.isNull(adminCount)?0:adminCount);
        info.put("commonCount", Objects.isNull(commonCount)?0:commonCount);
        info.put("bookTypes", Objects.isNull(bookTypes)?0:bookTypes);
        info.put("books", books);
        request.setAttribute("info", info);
        return "welcome";
    }

    @GetMapping("/BookType")
    public String toBookType(HttpServletRequest request){
        long bookCount = tblBookMapper.getBookCount();
        //???????????????BookTypeInfo??????????????????
        List<BookTypeInfo> bookTypeInfos = tblBookService.getBookTypeInfo();
        request.setAttribute("bookTypeInfos", bookTypeInfos);
        request.setAttribute("bookCount", bookCount);
        return "public/BookType";
    }

    @GetMapping("/EditInfo")
    public String toEditInfo(){
        return "public/EditInfo";
    }

    @GetMapping("/SelfInfo")
    public String toSelfInfo(){
        return "public/SelfInfo";
    }

    @PostMapping("/editInfo")
    @ResponseBody
    public Result editInfo(TblUser user, HttpServletRequest request){
        //???????????????user??????
        if(user != null){
            TblUser oldUser = tblUserMapper.selectById(user.getUid());
            if(oldUser != null){
                oldUser.setUsername(user.getUsername());
                oldUser.setPassword(user.getPassword());
                oldUser.setEmail(user.getEmail());
                oldUser.setPhone(user.getPhone());
                int i = tblUserMapper.updateById(oldUser);
                if(i > 0){
                    Result result = Result.success(200, "????????????", oldUser);
                    //??????session??????
                    request.getSession().setAttribute("result", result);
                    return result;
                }else{
                    return Result.fail(404, "????????????", oldUser);
                }
            }else{
                return Result.fail(404, "??????????????????", oldUser);
            }
        }else{
            return Result.fail(404, "????????????", null);
        }
    }

    @GetMapping("/checkUsername2")
    @ResponseBody
    public boolean checkUsername(String username){
        return tblUserMapper.checkUsernameExist(username) == 0;
    }

    @GetMapping("/root/toAddAdmin")
    public String toAddAdmin(){
        return "root/addAdmin";
    }

    @GetMapping("/root/toDelAdmin")
    public String toDelAdmin(HttpServletRequest request){
        //??? admin ????????????
        List<TblUser> admins = tblUserMapper.getAdmins();
        request.setAttribute("admins", admins);
        return "root/delAdmin";
    }

    @GetMapping("/admin/toAddBook")
    public String toAddBook(HttpServletRequest request){
        //?????????????????????request??????
        List<TblBookType> types = tblBookTypeMapper.selectList(null);
        if(types != null && types.size() > 0){
            request.setAttribute("types", types);
        }
        return "public/addBook";
    }

    @GetMapping("/admin/toSearchBook")
    public String toSearchBook(HttpServletRequest request){
        //???????????????????????????????????????
        List<TblBookType> types = tblBookTypeMapper.selectList(null);
        if(types != null && types.size() > 0){
            request.setAttribute("types", types);
        }
        return "public/searchBook";
    }

    @GetMapping("/admin/toBookTypeList")
    public String toBookTypeList(HttpServletRequest request){
        //???????????????????????????????????????
        List<TblBookType> types = tblBookTypeMapper.selectList(null);
        if(types != null && types.size() > 0){
            request.setAttribute("types", types);
        }
        return "public/bookTypelist";
    }

    @GetMapping("/admin/FlushSystem")
    @ResponseBody
    public boolean FlushSystem(){
        //???????????????????????? ajax ??????, ??????session??????????????????????????????
        //????????? session ??????????????????????????????????????????????????????????????????, ??????????????????????????????????????????
        //????????????, ???????????????????????? true,????????????????????????
        return true;
    }
}
