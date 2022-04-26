package com.blog.demo.service;

import com.blog.demo.dto.TagBackDTO;
import com.blog.demo.dto.TagDTO;
import com.blog.demo.dto.TagOptionDTO;
import com.blog.demo.entity.Tag;
import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.demo.vo.ConditionVo;
import com.blog.demo.vo.PageResult;
import com.blog.demo.vo.TagVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liziwen
 * @since 2022-03-16
 */
public interface TagService extends IService<Tag> {

    List<TagOptionDTO> listTagsBySearch(ConditionVo conditionVo);

    /**
     * 返回后台分类列表
     * @param conditionVo
     * @return
     */
    PageResult<TagBackDTO> listBackTags(ConditionVo conditionVo);

    /**
     * 新增或更新后台标签
     * @param tagVO
     */
    void saveOrUpdateTag(TagVO tagVO);

    /**
     * 删除标签列表
     * @param idList
     */
    void deleteTags(List<Integer> idList);

    PageResult<TagDTO> listTags(ConditionVo conditionVo);
}
