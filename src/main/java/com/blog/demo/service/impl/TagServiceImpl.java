package com.blog.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.blog.demo.dto.TagBackDTO;
import com.blog.demo.dto.TagDTO;
import com.blog.demo.dto.TagOptionDTO;
import com.blog.demo.entity.ArticleTag;
import com.blog.demo.entity.Tag;
import com.blog.demo.exception.BizException;
import com.blog.demo.mapper.ArticleTagMapper;
import com.blog.demo.mapper.TagMapper;
import com.blog.demo.service.TagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.demo.util.BeanCopyUtils;
import com.blog.demo.util.PageUtils;
import com.blog.demo.vo.ConditionVo;
import com.blog.demo.vo.PageResult;
import com.blog.demo.vo.TagVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liziwen
 * @since 2022-03-16
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private ArticleTagMapper articleTagMapper;

    @Override
    public List<TagOptionDTO> listTagsBySearch(ConditionVo conditionVo) {
        List<Tag> tagList = tagMapper.selectList(new LambdaQueryWrapper<Tag>()
                .like(StringUtils.isNotBlank(conditionVo.getKeywords()), Tag::getTagName, conditionVo.getKeywords())
                .orderByDesc(Tag::getId));

        return BeanCopyUtils.copyList(tagList, TagOptionDTO.class);
    }

    @Override
    public PageResult<TagBackDTO> listBackTags(ConditionVo conditionVo) {

        Integer count = tagMapper.selectCount(new LambdaQueryWrapper<Tag>()
                .like(StringUtils.isNotBlank(conditionVo.getKeywords()), Tag::getTagName, conditionVo.getKeywords()));
        if(count <= 0){
            return new PageResult<>();
        }
        List<TagBackDTO> tagBackDTOS = tagMapper.listTagBackDTO(PageUtils.getLimitCurrent(), PageUtils.getSize(), conditionVo);
        return new PageResult<>(tagBackDTOS,count);
    }

    @Override
    public void saveOrUpdateTag(TagVO tagVO) {
        Tag existTag = tagMapper.selectOne(new LambdaQueryWrapper<Tag>().select(Tag::getId)
                .eq(Tag::getTagName, tagVO.getTagName()));
        if(Objects.nonNull(existTag) && !existTag.getId().equals(tagVO.getId())){
            throw new BizException("标签名已经存在");
        }
        Tag tag = BeanCopyUtils.copyObject(tagVO, Tag.class);
        this.saveOrUpdate(tag);
    }

    @Override
    public void deleteTags(List<Integer> idList) {
        Integer count = articleTagMapper.selectCount(new LambdaQueryWrapper<ArticleTag>()
                .in(ArticleTag::getTagId, idList));
        if(count > 0){
            throw new BizException("删除失败，标签下仍有文章");
        }
        tagMapper.deleteBatchIds(idList);
    }

    @Override
    public PageResult<TagDTO> listTags(ConditionVo conditionVo) {
        Integer count = tagMapper.selectCount(new LambdaQueryWrapper<Tag>()
                .like(StringUtils.isNotBlank(conditionVo.getKeywords()), Tag::getTagName, conditionVo.getKeywords()));
        if(count <= 0){
            return new PageResult<>();
        }
        List<Tag> tags = tagMapper.selectList(null);
        List<TagDTO> tagDTOS = BeanCopyUtils.copyList(tags, TagDTO.class);
        return new PageResult<>(tagDTOS,count);
    }
}
