package com.blog.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.demo.dto.FriendLinkBackDTO;
import com.blog.demo.dto.FriendLinkDTO;
import com.blog.demo.entity.FriendLink;
import com.blog.demo.mapper.FriendLinkMapper;
import com.blog.demo.service.FriendLinkService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.demo.util.BeanCopyUtils;
import com.blog.demo.util.PageUtils;
import com.blog.demo.vo.ConditionVo;
import com.blog.demo.vo.FriendLinkVO;
import com.blog.demo.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liziwen
 * @since 2022-04-21
 */
@Service
public class FriendLinkServiceImpl extends ServiceImpl<FriendLinkMapper, FriendLink> implements FriendLinkService {

    @Autowired
    private FriendLinkMapper friendLinkMapper;

    @Override
    public PageResult<FriendLinkBackDTO> listFriendLinkDTO(ConditionVo conditionVo) {
        //根据页信息查询
        Page<FriendLink> page = new Page<>(PageUtils.getCurrent(),PageUtils.getSize());
        Page<FriendLink> friendLinkPage = friendLinkMapper
                .selectPage(page,
                        new LambdaQueryWrapper<FriendLink>()
                                .like(StringUtils.isNotBlank(conditionVo.getKeywords()), FriendLink::getLinkName, conditionVo.getKeywords()));
        List<FriendLink> friendLinkList = friendLinkPage.getRecords();
        //转化成数据传输类
        List<FriendLinkBackDTO> friendLinkBackDTOS = BeanCopyUtils.copyList(friendLinkList, FriendLinkBackDTO.class);
        return new PageResult<>(friendLinkBackDTOS,(int)friendLinkPage.getTotal());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdateFriendLink(FriendLinkVO friendLinkVO) {
        FriendLink friendLink = BeanCopyUtils.copyObject(friendLinkVO, FriendLink.class);
        this.saveOrUpdate(friendLink);
    }

    @Override
    public List<FriendLinkDTO> listFriendLink() {
        List<FriendLink> friendLinkList = friendLinkMapper.selectList(null);
        return BeanCopyUtils.copyList(friendLinkList, FriendLinkDTO.class);
    }
}
