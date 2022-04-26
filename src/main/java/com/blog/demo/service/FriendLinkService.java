package com.blog.demo.service;

import com.blog.demo.dto.FriendLinkBackDTO;
import com.blog.demo.dto.FriendLinkDTO;
import com.blog.demo.entity.FriendLink;
import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.demo.vo.ConditionVo;
import com.blog.demo.vo.FriendLinkVO;
import com.blog.demo.vo.PageResult;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liziwen
 * @since 2022-04-21
 */
public interface FriendLinkService extends IService<FriendLink> {

    PageResult<FriendLinkBackDTO> listFriendLinkDTO(ConditionVo conditionVo);

    void saveOrUpdateFriendLink(FriendLinkVO friendLinkVO);

    List<FriendLinkDTO> listFriendLink();
}
