package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Business;
import com.example.entity.Collect;
import com.example.entity.User;
import com.example.mapper.CollectMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 业务处理
 **/
@Service
public class CollectService {

    @Resource
    private CollectMapper collectMapper;

    @Resource
    private BusinessService businessService;

    @Resource
    private UserService userService;

    /**
     * 新增
     */
    public void add(Collect collect) {
        collect.setTime(DateUtil.now());
        collectMapper.insert(collect);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        collectMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            collectMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Collect collect) {
        collectMapper.updateById(collect);
    }

    /**
     * 根据ID查询
     */
    public Collect selectById(Integer id) {
        return collectMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Collect> selectAll(Collect collect) {
        List<Collect> collectList = collectMapper.selectAll(collect);
        for (Collect c : collectList) {
            Business business = businessService.selectById(c.getBusinessId());
            c.setBusiness(business);
        }
        return collectList;
    }

    /**
     * 分页查询
     */
    public PageInfo<Collect> selectPage(Collect collect, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.BUSINESS.name().equals(currentUser.getRole())) {
            collect.setBusinessId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Collect> list = collectMapper.selectAll(collect);
        for (Collect c : list) {
            Business business = businessService.selectById(c.getBusinessId());
            c.setBusiness(business);
            User user = userService.selectById(c.getUserId());
            c.setUser(user);
        }
        return PageInfo.of(list);
    }

    public Collect selectByBusinessId(Integer businessId, Integer userId) {
        return collectMapper.selectByBusinessId(businessId, userId);
    }

    public void deleteByCondition(Integer businessId, Integer userId) {
        collectMapper.deleteByCondition(businessId, userId);
    }

    /**
     * 通过用户id查询收藏商家和不包含在list的商家数据
     * @param id 用户id
     * @param list 商家id
     * @return
     */
    public List<Business> getListBrowsingByUserId(Integer id, List<Integer> list) {
        return collectMapper.getListBrowsingByUserId(id, list);
    }
}
