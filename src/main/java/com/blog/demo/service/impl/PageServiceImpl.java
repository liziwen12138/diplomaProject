package com.blog.demo.service.impl;

import com.blog.demo.entity.Page;
import com.blog.demo.mapper.PageMapper;
import com.blog.demo.service.PageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.demo.util.BeanCopyUtils;
import com.blog.demo.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 页面 服务实现类
 * </p>
 *
 * @author liziwen
 * @since 2022-03-25
 */
@Service
public class PageServiceImpl extends ServiceImpl<PageMapper, Page> implements PageService {

    @Autowired
    private PageMapper pageMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<PageVO> listPages() {
        List<PageVO> pageVOList = BeanCopyUtils.copyList(pageMapper.selectList(null), PageVO.class);
        //后续可以放置到redis缓存中
        return pageVOList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdatePage(PageVO pageVO) {
        Page page = BeanCopyUtils.copyObject(pageVO, Page.class);
        this.saveOrUpdate(page);
        //后续可以将页面存入redis
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deletePage(Integer pageId) {
        pageMapper.deleteById(pageId);
    }
}
