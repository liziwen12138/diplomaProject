package com.blog.demo.service;

import com.blog.demo.dto.LabelOptionDTO;
import com.blog.demo.dto.ResourceDTO;
import com.blog.demo.entity.Resource;
import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.demo.vo.ConditionVo;
import com.blog.demo.vo.ResourceVO;
import com.blog.demo.vo.Result;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liziwen
 * @since 2022-04-15
 */
public interface ResourceService extends IService<Resource> {

    /**
     * 获取资源选项列表
     * @return
     */
    List<LabelOptionDTO> listResourceOption();

    /**
     * 获取资源列表
     * @param conditionVo
     * @return
     */
    List<ResourceDTO> listResources(ConditionVo conditionVo);

    /**
     * 新增或更新资源
     * @param resourceVO
     */
    void saveOrUpdateByIdList(ResourceVO resourceVO);

    /**
     * 根据id删除资源
     * @param resourceId
     */
    void deleteResource(Integer resourceId);
}
