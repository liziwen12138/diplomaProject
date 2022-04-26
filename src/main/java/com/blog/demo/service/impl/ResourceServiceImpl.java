package com.blog.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.blog.demo.constant.CommonConst;
import com.blog.demo.dto.LabelOptionDTO;
import com.blog.demo.dto.ResourceDTO;
import com.blog.demo.entity.Resource;
import com.blog.demo.entity.RoleResource;
import com.blog.demo.exception.BizException;
import com.blog.demo.handler.FilterInvocationSecurityMetadataSourceImpl;
import com.blog.demo.mapper.ResourceMapper;
import com.blog.demo.mapper.RoleResourceMapper;
import com.blog.demo.service.ResourceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.demo.util.BeanCopyUtils;
import com.blog.demo.vo.ConditionVo;
import com.blog.demo.vo.ResourceVO;
import com.blog.demo.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liziwen
 * @since 2022-04-15
 */
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements ResourceService {

    @Autowired
    private ResourceMapper resourceMapper;

    @Autowired
    private RoleResourceMapper roleResourceMapper;

    @Autowired
    private FilterInvocationSecurityMetadataSourceImpl filterInvocationSecurityMetadataSource;

    @Override
    public List<LabelOptionDTO> listResourceOption() {
        //获取labelOption所需要的所有资源数据：非匿名允许的）
        List<Resource> resourceList = resourceMapper.selectList(new LambdaQueryWrapper<Resource>()
                .select(Resource::getId, Resource::getResourceName, Resource::getParentId)
                .eq(Resource::getIsAnonymous, CommonConst.FALSE));
        //获取所有的顶级资源模块
        List<Resource> parentResourceList = resourceList.stream()
                .filter(item -> Objects.isNull(item.getParentId())).collect(Collectors.toList());
        //根据parentId将所有的子资源模块分组
        Map<Integer, List<Resource>> childrenResourceList = resourceList.stream()
                .filter(item -> Objects.nonNull(item.getParentId())).collect(Collectors.groupingBy(Resource::getParentId));
        //整合所有资源模块并返回                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  
        return parentResourceList.stream().map(item -> {
            List<LabelOptionDTO> list = new ArrayList<>();
            List<Resource> children = childrenResourceList.get(item.getId());
            if (CollectionUtils.isNotEmpty(children)) {
                list = children.stream()
                        .map(resource -> LabelOptionDTO.builder()
                                .id(resource.getId())
                                .label(resource.getResourceName())
                                .build())
                        .collect(Collectors.toList());
            }
            return LabelOptionDTO.builder().id(item.getId()).label(item.getResourceName()).children(list).build();
        }).collect(Collectors.toList());
    }

    @Override
    public List<ResourceDTO> listResources(ConditionVo conditionVo) {
        //查询资源列表，如果存在关键词则根据关键词搜索列表
        List<Resource> resourceList = resourceMapper
                .selectList(new LambdaQueryWrapper<Resource>().like(StringUtils.isNotBlank(conditionVo.getKeywords()), Resource::getResourceName, conditionVo.getKeywords()));
        //获取所有根资源模块
        List<Resource> parentResourceList = listResourcesModule(resourceList);
        //获取所有子资源模块（按照根资源id以map形式存储）
        Map<Integer, List<Resource>> childrenResourcesMap = listChildrenResourcesModule(resourceList);
        //将所有子资源与根资源进行绑定
        List<ResourceDTO> resourceDTOList = parentResourceList.stream().map(item -> {
            ResourceDTO parent = BeanCopyUtils.copyObject(item, ResourceDTO.class);
            List<ResourceDTO> children = BeanCopyUtils.copyList(childrenResourcesMap.get(parent.getId()), ResourceDTO.class);
            parent.setChildren(children);
            childrenResourcesMap.remove(parent.getId());
            return parent;
        }).collect(Collectors.toList());
        //如果存在子资源有parentId，但是根资源不存在的情况，将该子资源拼接到根资源目录
        if (CollectionUtils.isNotEmpty(childrenResourcesMap)) {
            List<Resource> childrenList = new ArrayList<>();
            childrenResourcesMap.values().forEach(childrenList::addAll);
            List<ResourceDTO> childrenDTOList = childrenList
                    .stream()
                    .map(item -> BeanCopyUtils.copyObject(item, ResourceDTO.class))
                    .collect(Collectors.toList());
            resourceDTOList.addAll(childrenDTOList);
        }
        return resourceDTOList;
    }

    @Override
    public void saveOrUpdateByIdList(ResourceVO resourceVO) {
        Resource resource = BeanCopyUtils.copyObject(resourceVO, Resource.class);
        this.saveOrUpdate(resource);
        //重新加载角色资源信息
        filterInvocationSecurityMetadataSource.clearDataSource();
    }

    @Override
    public void deleteResource(Integer resourceId) {
        //检查是否有用户与角色相关联：存在关联则抛出异常
        Integer count = roleResourceMapper.selectCount(new LambdaQueryWrapper<RoleResource>().eq(RoleResource::getResourceId, resourceId));
        if(count > 0){
            throw new BizException("存在角色与该资源相关联，删除失败");
        }
        //先删除资源下的子资源
        List<Resource> resourceList = resourceMapper.selectList(new LambdaQueryWrapper<Resource>().select(Resource::getId).eq(Resource::getParentId, resourceId));
        List<Integer> resourceIdList = resourceList.stream().map(Resource::getId).collect(Collectors.toList());
        //删除目标资源，即将当前资源id加入待删除列表
        resourceIdList.add(resourceId);
        resourceMapper.deleteBatchIds(resourceIdList);
    }

    /**
     * 获取所有resourcesList中根资源列表
     * @param resourceList
     * @return
     */
    public List<Resource> listResourcesModule(List<Resource> resourceList){
        return resourceList
                .stream()
                .filter(item -> Objects.isNull(item.getParentId()))
                .collect(Collectors.toList());
    }

    /**
     * 根据parent id以map形式存储子资源并返回
     * @param resourceList
     * @return
     */
    public Map<Integer, List<Resource>> listChildrenResourcesModule(List<Resource> resourceList){
        return resourceList
                .stream()
                .filter(item -> Objects.nonNull(item.getParentId()))
                .collect(Collectors.groupingBy(Resource::getParentId));
    }
}
