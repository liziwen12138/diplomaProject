package com.blog.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blog.demo.dto.CategoryBackDTO;
import com.blog.demo.dto.CategoryDTO;
import com.blog.demo.dto.CategoryOptionDTO;
import com.blog.demo.entity.Article;
import com.blog.demo.entity.Category;
import com.blog.demo.exception.BizException;
import com.blog.demo.mapper.ArticleMapper;
import com.blog.demo.mapper.CategoryMapper;
import com.blog.demo.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.demo.util.BeanCopyUtils;
import com.blog.demo.util.PageUtils;
import com.blog.demo.vo.CategoryVo;
import com.blog.demo.vo.ConditionVo;
import com.blog.demo.vo.PageResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public List<CategoryOptionDTO> listCategoriesBySearch(ConditionVo conditionVo) {
        List<Category> categoryList = categoryMapper.selectList(new LambdaQueryWrapper<Category>()
                .like(StringUtils.isNotBlank(conditionVo.getKeywords()), Category::getCategoryName,conditionVo.getKeywords())
                .orderByDesc(Category::getId));
        return BeanCopyUtils.copyList(categoryList, CategoryOptionDTO.class);
    }

    @Override
    public PageResult<CategoryBackDTO> listBackCategories(ConditionVo conditionVo) {
        //查询分类的数量
        Integer count = categoryMapper.selectCount(new LambdaQueryWrapper<Category>()
                .like(StringUtils.isNotBlank(conditionVo.getKeywords()), Category::getCategoryName, conditionVo.getKeywords()));
        //如果为空则返回空的分页返回接口类
        if(count <= 0){
            return new PageResult<>();
        }
        //如果不为空，则根据conditionVO中的分页信息，从mapper中获取分类信息
//        System.out.println(PageUtils.getLimitCurrent());
//        System.out.println(PageUtils.getSize());
        List<CategoryBackDTO> categoryBackDTOS = categoryMapper.listCategoryBackDTO(PageUtils.getLimitCurrent(), PageUtils.getSize(), conditionVo);
        return new PageResult<>(categoryBackDTOS, count);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdateCategory(CategoryVo categoryVo) {
        Category existCategory = categoryMapper.selectOne(new LambdaQueryWrapper<Category>().select(Category::getId).eq(StringUtils.isNotBlank(categoryVo.getCategoryName()), Category::getCategoryName, categoryVo.getCategoryName()));
        if(Objects.nonNull(existCategory) && !existCategory.getId().equals(categoryVo.getId())){
            throw new BizException("分类名已存在");
        }
        Category category = Category.builder().id(categoryVo.getId())
                .categoryName(categoryVo.getCategoryName()).build();
        this.saveOrUpdate(category);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteCategories(List<Integer> idList) {
        Integer count = articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                .in(Article::getCategoryId, idList));
        if(count > 0){
            throw new BizException("删除失败，分类下仍存在文章");
        }
        categoryMapper.deleteBatchIds(idList);
    }

    @Override
    public PageResult<CategoryDTO> listCategories(ConditionVo conditionVo) {
        Integer count = categoryMapper.selectCount(new LambdaQueryWrapper<Category>()
                .like(StringUtils.isNotBlank(conditionVo.getKeywords()), Category::getCategoryName, conditionVo.getKeywords()));
        if(count <= 0){
            return new PageResult<>();
        }
        return new PageResult<CategoryDTO>(categoryMapper.listCategories(),count);
    }

}
