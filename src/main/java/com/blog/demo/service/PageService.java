package com.blog.demo.service;

import com.blog.demo.entity.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.demo.vo.PageVO;

import java.util.List;

/**
 * <p>
 * 页面 服务类
 * </p>
 *
 * @author liziwen
 * @since 2022-03-25
 */
public interface PageService extends IService<Page> {

    /**
     *获取页面列表
     * @return
     */
    List<PageVO> listPages();

    void saveOrUpdatePage(PageVO pageVO);

    void deletePage(Integer pageId);
}
