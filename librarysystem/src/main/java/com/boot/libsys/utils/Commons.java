package com.boot.libsys.utils;

import com.boot.libsys.entity.TblBook;
import com.vdurmont.emoji.EmojiParser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 页面数据展示封装类
 */

@Component
@SuppressWarnings("unused")
public class Commons {
    /**
     * 网站链接
     *
     * @return String
     */
    public static String site_url() {
        return site_url("/book/1");
    }
    /**
     * 返回网站链接下的全址
     *
     * @param sub 后面追加的地址
     * @return String
     */
    public static String site_url(String sub) {
        return site_option("site_url") + sub;
    }

    /**
     * 网站配置项
     *
     * @param key key
     * @return String
     */
    public static String site_option(String key) {
        return site_option(key, "");
    }

    /**
     * 网站配置项
     *
     * @param key key
     * @param defalutValue 默认值
     * @return String
     */
    public static String site_option(String key, String defalutValue) {
        if (StringUtils.isBlank(key)) {
            return "";
        }
        return defalutValue;
    }

    /**
     * 截取文章摘要
     *
     * @param book 图书
     * @param len   要截取文字的个数
     * @return String
     */
    public static String intro(TblBook book, int len) {
        String value = book.getContent();
        int pos = value.indexOf("<!--more-->");
        if (pos != -1) {
            String html = value.substring(0, pos);
            return MyUtils.htmlToText(MyUtils.mdToHtml(html));
        } else {
            String text = MyUtils.htmlToText(MyUtils.mdToHtml(value));
            if (text.length() > len) {
                return text.substring(0, len) + "......";
            }
            return text;
        }
    }

    /**
     * 截取字符串
     *
     * @param str dtr
     * @param len len
     * @return String
     */
    public static String substr(String str, int len) {
        if (str.length() > len) {
            return str.substring(0, len);
        }
        return str;
    }

    /**
     * 返回日期
     *
     * @return String
     */
     public static String dateFormat(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
     }

    /**
     * 返回文章链接地址
     *
     * @param aid aid
     * @return String
     */
    public static String permalink(String prefix, Integer aid) {
        return prefix + aid.toString();
    }


    /**
     * 对文章内容进行格式转换，将Markdown为Html
     * @param value value
     * @return  ok
     */
    public static String article(String value) {
        if (StringUtils.isNotBlank(value)) {
            value = value.replace("<!--more-->", "\r\n");
            return MyUtils.mdToHtml(value);
        }
        return "";
    }

    /**
     * 显示文章缩略图，
     *
     * @return String
     */
    public static String show_thumb(TblBook book) {
        return "/public/img/bookThumb/" + book.getBid() + ".png";
    }

    /**
     * 这种格式的字符转换为emoji表情
     * @param value value
     * @return String
     */
    public static String emoji(String value) {
        return EmojiParser.parseToUnicode(value);
    }

}
