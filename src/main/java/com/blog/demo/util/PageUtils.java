package com.blog.demo.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 分页工具类
 * 注意此类并没有子实现分页，只是封装了分页操作，提高代码的复用性
 */
@Component
public class PageUtils {
    /**
     * 用一个本地线程去管理分页情况，暂存当前页面的分页信息，保证多个线程之间互不影响。
     * 分页采用mybatisplus的分页管理去处理
     * PageHelper分页插件里面也是用ThreadLocal实现的
     */
    private static final ThreadLocal<Page<?>> PAGE_HOLDER = new ThreadLocal<>();

    /**
     * 在本地线程中存放当前页信息
     * @param page
     */
    public static void setCurrentPage(Page<?> page){
        PAGE_HOLDER.set(page);
    }

    /**
     * 得到myBatis-plus提供的page信息
     * @return
     */
    public static Page<?> getPage(){
        Page<?> page = PAGE_HOLDER.get();
//        此处防止页数据尚未存入本地线程，进行数据检验
        if(Objects.isNull(page)){
            setCurrentPage(new Page<>());
        }
        return PAGE_HOLDER.get();
    }

    /**
     * 从当前页信息中返回每个页的大小
     * @return
     */
    public static Long getSize(){
        return getPage().getSize();
    }

    /**
     * 从当前信息页中返回当前页号
     * @return
     */
    public static Long getCurrent(){
        return getPage().getCurrent();
    }

    /**
     * 得到之前页的页面内容数量，
     * （就是返回当前页的索引开始）
     * @return
     */
    public static Long getLimitCurrent(){
        Long current = getCurrent();
        System.out.println("thread: "+ PAGE_HOLDER.get());
        return (getCurrent() - 1) * getSize();
    }

    public static void remove(){
        PAGE_HOLDER.remove();
    }

}
