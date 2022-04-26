package com.blog.demo.mapper;

import com.blog.demo.dto.TagBackDTO;
import com.blog.demo.entity.Tag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.demo.vo.ConditionVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liziwen
 * @since 2022-03-16
 */
@Mapper
@Component(value = "tagMapper")
public interface TagMapper extends BaseMapper<Tag> {

    /**
     * 返回后台标签列表
     * @param limitCurrent
     * @param size
     * @param conditionVo
     * @return
     */
    List<TagBackDTO> listTagBackDTO(@Param("current")Long limitCurrent,@Param("size")Long size,@Param("condition")ConditionVo conditionVo);
}
