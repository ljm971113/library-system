package com.boot.libsys.interceptor;

import com.boot.libsys.entity.TblUser;
import com.boot.libsys.utils.Result;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author Lucky
 * @version 1.0
 * @since 2020/06/10 10:46
 */

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //这个拦截器所需要的功能:
        //1. 如果用户没有登录系统, 直接重定向到登录页面
        //2. 如果用户是登录了的用户, 就直接跳转到相应的首页面
        //3. 判断当前用户是否越级访问, 如果越级访问, 就需要重定向到403页面, 提示权限不足信息
        String uri = request.getRequestURI();
        System.out.println("拦截的URI:" + uri);
        Result result = (Result)request.getSession().getAttribute("result");
        if (null == result){
            //重定向到登录页面
            response.sendRedirect(request.getContextPath() + "/");
            return false;
        }
        //已经登录的用户, 首先判断是否是越级访问
        //1. 获取 user 并且判断是否为空
        Object data = result.getData();
        if(null != data){
            TblUser user = (TblUser) data;
            //获取权限
            String authority = user.getAuthority().split("_")[1];// root or admin , common
            if(authority.equals("common")){
                if (uri.contains("root") || uri.contains("admin")) {//普通用户越级访问
                    response.sendRedirect(request.getContextPath() + "/error_403");
                    return false;
                }
            }
        }
        return true;
    }
}
