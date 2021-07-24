package com.boot.libsys.controller.root;

import com.boot.libsys.entity.TblUser;
import com.boot.libsys.mapper.TblUserMapper;
import com.boot.libsys.utils.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * @author Lucky
 * @version 1.0
 * @since 2020/06/04 13:09
 */
@Controller
@RequestMapping("/root")
public class RootController {

    @Resource
    private TblUserMapper tblUserMapper;

    @GetMapping("/index")
    public String toIndex(){
        return "root/index";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) throws ServletException {
        request.getSession().invalidate();
        request.logout();
        return "redirect:/";
    }

    @PostMapping("/addAdmin")
    @ResponseBody
    public Result addAdmin(TblUser user){
        //获取原先的user对象
        if(user != null){
            user.setAuthority("ROLE_admin");
            user.setSince(LocalDate.now());
            int i = tblUserMapper.insert(user);
            if(i > 0){
                return Result.success(200, "添加成功", user);
            }else{
                return Result.fail(404, "修改失败", user);
            }
        }else{
            return Result.fail(404, "操作错误", null);
        }
    }

    @GetMapping("/delAdmin")
    @ResponseBody
    public Result delAdmin(@RequestParam("ids[]")Integer[] ids){
        System.out.println("异步请求数据: "+Arrays.toString(ids));
        //删除逻辑就是, 提交上来的数据是需要更改的, 获取的数据的状态是启用改为禁用
        //是禁用就改为启用
        int updateCount = 0;
        if(ids != null && ids.length != 0){
            List<TblUser> users = tblUserMapper.getUser(ids);
            for (TblUser user : users) {
                updateCount += tblUserMapper.DelAdmin(user.getStatus() == 1 ? 0 : 1 ,user.getUid());
            }
            if(updateCount == ids.length){
                return Result.success(200, "操作成功", null);
            }else if (updateCount == 0){
                return Result.fail(404, "操作失败", null);
            }else if(updateCount > 0 && updateCount < ids.length){
                return Result.fail(404, "部分失败,请刷新页面", null);
            }
        }
        return Result.fail(404, "数据错误", null);
    }
}
