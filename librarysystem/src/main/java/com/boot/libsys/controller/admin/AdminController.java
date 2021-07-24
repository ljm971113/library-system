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

    //获取读者信息, 显示在页面上
    @GetMapping("/userList")
    public String toUserList(HttpServletRequest request){
        //将读者信息放在request域中
        List<TblUser> commons = tblUserMapper.getCommons();
        System.out.println("commons:" + commons);
        request.setAttribute("commons", commons);
        return "public/userList";
    }

    //删除 读者
    @GetMapping("/delCommon")
    @ResponseBody
    public Result delCommon(@RequestParam("ids[]")Integer[] ids, HttpServletRequest request){
        System.out.println(Arrays.toString(ids));
        if(ids != null && ids.length > 0){
            List<TblUser> commons = tblUserMapper.getCommons();
            if(commons != null){
                int i = tblUserMapper.deleteBatchIds(Arrays.asList(ids));
                if(i == ids.length){
                    return Result.success(200, "操作成功", null);
                }else{
                    return Result.fail(405, "部分失败,请刷新页面", null);
                }
            }else{
                return Result.fail(404, "获取数据错误,请重试", null);
            }

        }
        return Result.fail(404, "数据错误", null);
    }

    @GetMapping("/bookList")
    public String bookList(HttpServletRequest request){
        //分页获取, 默认一开始显示的第1页, 一页显示10条
        return this.bookList(request, 1, 10);
    }

    @GetMapping("/bookList/page/{page}")
    public String bookList(HttpServletRequest request, @PathVariable("page")int page, @RequestParam(value = "size",defaultValue = "10")int size){
        //将图书信息传到域中
        PageInfo<TblBook> bookPageInfo = tblBookService.getBooksByPage(page, size);
        request.getSession().setAttribute("bookPageInfo", bookPageInfo);
        //设置工具类
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
            //重新设置 request 域中的 booklist
            PageInfo<TblBook> bookPageInfo = (PageInfo<TblBook>)request.getSession().getAttribute("bookPageInfo");
            List<TblBook> bookList = bookPageInfo.getList();
            for (int m = 0; m < bookList.size(); ++m) {
                if(bookList.get(m).getBid().equals(book.getBid())){
                    bookList.set(m, book);
                    break;
                }
            }
            request.getSession().setAttribute("bookPageInfo", bookPageInfo);
            return Result.success(200, flag ? "新增成功!" : "下架成功!", null);
        }else{
            return Result.fail(404, flag ? "新增失败!" : "下架失败!", null);
        }
    }

    @GetMapping("/delBookCountBatch")
    @ResponseBody
    public Result delBookCountBatch(@RequestParam("ids[]")Integer[] info, HttpServletRequest request){
        System.out.println("批量修改的 ID:" + Arrays.toString(info));
        int succ = 0;
        //修改步骤
        //遍历每一个 id, 然后调用 delBookCount() 方法修改
        for (Integer id : info) {
            TblBook book = tblBookService.getById(id);
            //直接设置成借出去的数量
            succ += tblBookReservesMapper.addBookCount(id, book.getTblBookReserves().getBorrowed());
        }
        if(succ == 0){
            return Result.fail(404, "所选的所有图书下架失败!", null);
        }else if(succ > 0 && succ < info.length){
            return Result.fail(404, "部分图书下架失败, 请重试未成功的图书!", null);
        }else{
            return Result.success(200, "所选的所有图书下架成功!", null);
        }
    }

    @PostMapping("/addBook")
    public String addBook(TblBook book, Integer number, HttpServletRequest request){
        System.out.println(book + ":" + number);//打印信息
        if(book != null && number != null && number > 0){
            int i = tblBookService.save(book) ? 1 : 0;//添加书籍
            //添加图书初始信息, tbl_book_re... 表
            TblBookReserves reserves = new TblBookReserves();
            reserves.setBid(book.getBid());
            reserves.setTotal(number);
            reserves.setBorrowed(0);
            int j = tblBookReservesMapper.insert(reserves);
            if((i + j) == 2){
                request.setAttribute("res",Result.success(200, "添加书籍成功", null));
            }else{
                request.setAttribute("res",Result.fail(404, "数据保存错误, 请重试", null));
            }
            //将类型信息放在request域中
            List<TblBookType> types = tblBookTypeMapper.selectList(null);
            if(types != null && types.size() > 0){
                request.setAttribute("types", types);
            }
            return "public/addBook";
        }
        request.setAttribute("res",Result.fail(404, "请求数据错误, 请重试", null));
        //将类型信息放在request域中
        List<TblBookType> types = tblBookTypeMapper.selectList(null);
        if(types != null && types.size() > 0){
            request.setAttribute("types", types);
        }

        return "public/addBook";
    }

    @PostMapping("/searchBook")
    public String searchBook(Integer bid, String bname, String author, String companyname, Integer typeid, HttpServletRequest request){

        System.out.println(bid + ":" + bname + ":" + author + ":" + companyname + ":" + typeid);

        //获取到查询信息
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
        //此时查询出来的bookList没有设置类别信息等, 需要对此进行填充
        bookList = tblBookService.setBookAllColumn(bookList);
        //还需要新增 类型信息
        //将图书类别信息放在查询页面
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
        //获取现有的类型
        //将图书类别信息放在查询页面
        if(StringUtils.isNotEmpty(newTypeName)){
            List<TblBookType> types = tblBookTypeMapper.selectList(null);
            for (TblBookType type : types) {
                if(type.getTypename().equals(newTypeName)){
                    return Result.fail(404, "已有分类,请勿重复添加!", null);
                }
            }
            //说明没有, 则添加
            TblBookType type = new TblBookType();
            type.setTypename(newTypeName);

            int val = tblBookTypeMapper.insert(type);

            if(val > 0){
                //重新设定request中的分类信息
                //将图书类别信息放在查询页面
                List<TblBookType> typesnew = tblBookTypeMapper.selectList(null);
                if(typesnew != null && types.size() > 0){
                    request.setAttribute("types", typesnew);
                }
                return Result.success(200, "添加 " + newTypeName + " 分类成功!", null);
            }else{
                return Result.fail(404, "添加 " + newTypeName + " 分类失败!", null);
            }
        }
        return Result.success(405, "数据错误, 请重试!", null);
    }
}