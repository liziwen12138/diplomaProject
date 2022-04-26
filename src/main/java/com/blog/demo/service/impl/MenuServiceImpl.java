package com.blog.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.blog.demo.dto.LabelOptionDTO;
import com.blog.demo.dto.MenuDTO;
import com.blog.demo.dto.UserMenuDTO;
import com.blog.demo.entity.Menu;
import com.blog.demo.entity.RoleMenu;
import com.blog.demo.exception.BizException;
import com.blog.demo.mapper.MenuMapper;
import com.blog.demo.mapper.RoleMenuMapper;
import com.blog.demo.service.MenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.demo.util.BeanCopyUtils;
import com.blog.demo.util.UserUtils;
import com.blog.demo.vo.ConditionVo;
import com.blog.demo.vo.MenuVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.blog.demo.constant.CommonConst.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.blog.demo.constant.CommonConst.COMPONENT;
import static com.blog.demo.constant.CommonConst.TRUE;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liziwen
 * @since 2022-03-12
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;


    /**
     * 获取菜单页表，采用线获取当前角色的全部页表后，在service中进行配置，转为DTO再传递给前端
     * @param conditionVo
     * @return
     */
    @Override
    public List<MenuDTO> listMenus(ConditionVo conditionVo) {
//        StringUtils.isNotBlank工具是用来判断参数是否未空
//        LambdaQueryWrapper.like 就是进行模糊匹配（默认左右模糊，设置需要使用apply()方法）
        List<Menu> menuList = menuMapper.selectList(new LambdaQueryWrapper<Menu>().like(StringUtils.isNotBlank(conditionVo.getKeywords()), Menu::getName, conditionVo.getKeywords()));
//        获取目录列表(即展现在菜单栏的顶级菜单)
        List<Menu> catalogList = listCatalog(menuList);
//        获取目录下的子菜单（有多个目录，每个目录有多个菜单，所以可以采用map或者set进行存储）
        Map<Integer,List<Menu>> childrenMap = getMenuMap(menuList);
//        组装目录菜单数据
        List<MenuDTO> menuDTOList = catalogList.stream().map(item -> {
            MenuDTO menuDTO = BeanCopyUtils.copyObject(item, MenuDTO.class);
//            获取目录下的菜单

//            List<MenuDTO> list = BeanCopyUtils.copyList(childrenMap.get(item.getId()), MenuDTO.class)
//                    .stream()
//                    .filter(childItem -> childItem != null && childItem.getId() != null)
//                    .sorted(Comparator.comparing(MenuDTO::getOrderNum))
//                    .collect(Collectors.toList());
            List<MenuDTO> list = BeanCopyUtils.copyList(childrenMap.get(item.getId()), MenuDTO.class);
            List<MenuDTO> collect = list.stream().
                    sorted(Comparator.comparing(MenuDTO::getOrderNum))
                    .collect(Collectors.toList());
//            将子菜单装填到目录的的children中
            menuDTO.setChildren(collect);
//            从子菜单与目录对照表中删除该目录，说明该目录已经构造完成
            childrenMap.remove(item.getId());
            return menuDTO;
        }).sorted(Comparator.comparing(MenuDTO::getOrderNum)).collect(Collectors.toList());
//      排除仍然有菜单未加载到目录中的情况，就是上述循环最后一次，或者说某个目录特殊情况并没有访问到时，将该目录再拼接到目录表中
        if(CollectionUtils.isNotEmpty(childrenMap)){
            List<Menu> childrenList = new ArrayList<>();
//            解释一下下一行代码：就是循环遍历childrenMap， 并将每个键值对中的值（即子菜单）添加到childrenList中
            childrenMap.values().forEach(childrenList::addAll);
//            此处注意stream().map() 实质就是遍历
            List<MenuDTO> childrenDTOList = childrenList.stream().map(item -> BeanCopyUtils.copyObject(item, MenuDTO.class))
                    .sorted(Comparator.comparing(MenuDTO::getOrderNum))
                    .collect(Collectors.toList());
            menuDTOList.addAll(childrenDTOList);
        }
        return menuDTOList;
    }


    @Override
    public List<UserMenuDTO> listUserMenus() {
//        获取当前用户的拥有权限的菜单列表
        Integer userInfoId = UserUtils.getLoginUser().getUserInfoId();
        List<Menu> menuList = menuMapper.listMenusByUserInfoId(userInfoId);
//        获取当前用户菜单中的目录菜单
        List<Menu> listCatalog = listCatalog(menuList);
//        生成每个目录所对应的子菜单映射表
        Map<Integer, List<Menu>> childrenMap = getMenuMap(menuList);
//        遍历每个目录，并将子菜单添加到目录的数据结构中的Children中
        List<UserMenuDTO> userMenuDTOS = listCatalog.stream().map(item -> {
//            生成目录（初始化返回给前端的目录视图）
            UserMenuDTO userMenuDTO = new UserMenuDTO();
            List<UserMenuDTO> list = new ArrayList<>();
            List<Menu> children = childrenMap.get(item.getId());
//          将子菜单存入到目录的children中（按菜单排序存放）
            if (CollectionUtils.isNotEmpty(children)) {
//                将原本目录为Menu转化为返回前端的数据结构userMenuDTO
                userMenuDTO = BeanCopyUtils.copyObject(item, UserMenuDTO.class);
                list = children.stream()
                        .sorted(Comparator.comparing(Menu::getOrderNum))
                        .map(menu -> {
//                            对每个子菜单进行加工，以返回给前端的数据结构传递
                            UserMenuDTO menuDTO = BeanCopyUtils.copyObject(menu, UserMenuDTO.class);
//                            子菜单在未打开时，设置为隐藏，不直接显示，只有在前端点击目录打开后，才显示子菜单
                            menuDTO.setHidden(menu.getIsHidden().equals(TRUE));
                            return menuDTO;
                        }).collect(Collectors.toList());
            } else {
//                如果没有子菜单的话，对一级菜单进行直接处理
                userMenuDTO.setPath(item.getPath());
                userMenuDTO.setComponent(COMPONENT);
//                子菜单就是自身
                list.add(UserMenuDTO.builder()
                        .path("")
                        .name(item.getName())
                        .icon(item.getIcon())
                        .component(item.getComponent())
                        .build());
            }
            userMenuDTO.setHidden(item.getIsHidden().equals(TRUE));
            userMenuDTO.setChildren(list);
            return userMenuDTO;
        }).collect(Collectors.toList());
        return userMenuDTOS;
    }

    @Override
    public List<LabelOptionDTO> listMenuOptions() {
        //查询菜单数据
        List<Menu> menuList = menuMapper.selectList(new LambdaQueryWrapper<Menu>().select(Menu::getId, Menu::getName, Menu::getParentId, Menu::getOrderNum));
        //获取目录列表
        List<Menu> catalogList = listCatalog(menuList);
        //获取目录下的子菜单
        Map<Integer, List<Menu>> childrenMap = getMenuMap(catalogList);
        //组装菜单
        return catalogList.stream().map(item -> {
            //获取目录下的菜单排序
            List<LabelOptionDTO> list = new ArrayList<>();
            List<Menu> children = childrenMap.get(item.getId());
            //按照顺序排序后装填到对应数据结构中
            if(CollectionUtils.isNotEmpty(children)){
                list = children.stream().sorted(Comparator.comparing(Menu::getOrderNum))
                        .map(menu -> LabelOptionDTO.builder().id(menu.getId()).label(menu.getName()).build())
                        .collect(Collectors.toList());
            }
            return LabelOptionDTO.builder().id(item.getId()).label(item.getName()).children(list).build();
        }).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdateMenu(MenuVO menuVO) {
        Menu menu = BeanCopyUtils.copyObject(menuVO, Menu.class);
        this.saveOrUpdate(menu);
    }

    @Transactional
    @Override
    public void deleteMenuById(Integer menuId) {
        //判断是否有角色与菜单关联，有则抛出异常
        Integer count = roleMenuMapper.selectCount(new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getMenuId, menuId));
        if (count > 0){
            throw new BizException("该菜单存在关联用户，删除失败");
        }
        //先删除子菜单
        List<Integer> menuIdList = menuMapper
                .selectList(new LambdaQueryWrapper<Menu>().select(Menu::getId).eq(Menu::getParentId, menuId)).stream().map(Menu::getId)
                .collect(Collectors.toList());
        //将选中菜单加入待删除列表
        menuIdList.add(menuId);
        menuMapper.deleteBatchIds(menuIdList);
    }

    /**
     * 获取每个目录下的子子菜单
     * @param menuList
     * @return
     */
    private Map<Integer, List<Menu>> getMenuMap(List<Menu> menuList) {
//        这里解释一下流式编码的内容意思：对传递的menuList进行遍历，过滤留下所有ParentId不为空的菜单（说明为子菜单）
//        然后利用集合工具.groupingBy()，根据Menu的ParentId进行分组存入Map集合{ParentId, List<Menu> list}。
//        此处的Menu::getParentId为函数式编程的写法，实际就是调用了Menu类中的getParentId()方法获得每个item的ParentId信息进行分组
//        注意：分组后的id值即为parentId
        return menuList.stream()
                .filter(item -> Objects.nonNull(item.getParentId()))
                .collect(Collectors.groupingBy(Menu::getParentId));
    }

    /**
     * 获取目录列表
     * @param menusList 菜单列表
     * @return 目录列表(实际就是顶级菜单，没有parentId的菜单)
     */
    private List<Menu> listCatalog(List<Menu> menusList) {
//        java8之后的流式写法配合lambda表达式
        return menusList.stream()
                .filter(item -> Objects.isNull(item.getParentId()))
                .sorted(Comparator.comparing(Menu::getOrderNum))
                .collect(Collectors.toList());
    }

}
