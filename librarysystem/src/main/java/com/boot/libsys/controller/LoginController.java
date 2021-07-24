package com.boot.libsys.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.boot.libsys.entity.TblUser;
import com.boot.libsys.mapper.TblUserMapper;
import com.boot.libsys.service.ITblUserService;
import com.boot.libsys.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;

/**
 * @author Lucky
 * @version 1.0
 * @since 2020/06/03 19:46
 */
@Controller
@SuppressWarnings("all")
public class LoginController {

    private final ITblUserService iTblUserService;
    private final TblUserMapper tblUserMapper;

    @Autowired
    public LoginController(ITblUserService iTblUserService, TblUserMapper tblUserMapper){
        this.iTblUserService = iTblUserService;
        this.tblUserMapper = tblUserMapper;
    }

    @PostMapping("/userLogin")
    public String userLogin(HttpSession session, HttpServletRequest request){
        String name = request.getParameter("username");
        String pwd = request.getParameter("password");
        TblUser temp = new TblUser();
        temp.setUsername(name);
        temp.setPassword(pwd);
        System.out.println("LoginController: " + temp.getUsername() + ", " + temp.getPassword());
        Wrapper<TblUser> wrapper = new QueryWrapper<>(temp);
        TblUser user = iTblUserService.getOne(wrapper);
        System.out.println("登录: user = " + user);
        if(user != null){
            //存放已登录标志
            session.setAttribute("logFlg", true);
            //校验正确
            session.setAttribute("result", Result.success(200, "登录成功", user));
            if(user.getAuthority().split("_")[1].equals("root") || user.getAuthority().split("_")[1].equals("admin")){
                return "redirect:/root/index";
            }
            return "redirect:" + user.getAuthority().split("_")[1] + "/index";
        }
        else{
            return "redirect:/login?error";
        }
    }

    @PostMapping("/register")
    public String register(TblUser user, HttpServletRequest request){
        // uid=null, username=qkm, gender=0, email=23232@qq,
        // phone=15678789999, password=2342, authority=ROLE_admin, since=null, status=null
        //设置user信息的完善属性
        user.setSince(LocalDate.now());
        //判断注册的类型, 如果是管理员, 将 status 字段置为0, 如果是读者, 直接插入
        if(user.getAuthority().equals("ROLE_admin")){
            //设置登录页面信息提示
            request.getSession().setAttribute("isAdmin", true);
            user.setStatus(1);
        }
        //将 user 写入数据库
        int i = tblUserMapper.insert(user);
        if(i > 0){
            //注册成功, 返回登录页面
            request.getSession().setAttribute("isSuccess", true);
            return "redirect:/login";
        }else {
            //注册成功, 返回登录页面
            request.getSession().setAttribute("isError", true);
            return "redirect:/login";
        }
    }
}
