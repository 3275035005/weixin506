package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.Constants;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.*;
import com.example.exception.CustomException;
import com.example.mapper.BusinessMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 商家表业务处理
 **/
@Service
public class BusinessService {

    @Resource
    private BusinessMapper businessMapper;

    @Resource
    private CollectService collectService;

    @Resource
    private CartService cartService;

    @Resource
    private OrdersService ordersService;

    @Resource
    private OrdersItemService ordersItemService;

    @Resource
    private BrowsingService browsingService;



    @Resource
    private CommentService commentService;

    /**
     * 新增
     */
    public void add(Business business) {
        Business dbBusiness = businessMapper.selectByUsername(business.getUsername());
        if (ObjectUtil.isNotNull(dbBusiness)) {
            throw new CustomException(ResultCodeEnum.USER_EXIST_ERROR);
        }
        if (ObjectUtil.isEmpty(business.getPassword())) {
            business.setPassword(Constants.USER_DEFAULT_PASSWORD);
        }
        if (ObjectUtil.isEmpty(business.getName())) {
            business.setName(business.getUsername());
        }
        businessMapper.insert(business);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        businessMapper.deleteById(id);
        // 删除购物车
        cartService.deleteByBusinessId(id);
        // 删除收藏
        collectService.deleteByCondition(id, null);
        //删除未结算的订单
        ordersService.updateByBusinessId(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Business business) {
        businessMapper.updateById(business);
    }

    /**
     * 根据ID查询基本
     */
    public Business selectBasicBusinessById(Integer id) {
        return businessMapper.selectById(id);
    }

    /**
     * 根据ID查询
     */
    public Business selectById(Integer id) {
        Account currentUser = TokenUtils.getCurrentUser();
        Collect collect = collectService.selectByBusinessId(id, currentUser.getId());
        Business business = businessMapper.selectById(id);
        business.setIsCollect(collect != null);
        wrapBusiness(business);
        return business;
    }

    /**
     * 查询所有
     */
    public List<Business> selectAll(Business business) {
        List<Business> businessList = businessMapper.selectAll(business);
        for (Business b : businessList) {
            wrapBusiness(b);
        }
        return businessList;
    }

    /**
     * 分页查询
     */
    public PageInfo<Business> selectPage(Business business, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Business> list = selectAll(business);
        return PageInfo.of(list);
    }

    /**
     * 登录
     */
    public Account login(Account account) {
        Account business = businessMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(business)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!account.getPassword().equals(business.getPassword())) {
            throw new CustomException(ResultCodeEnum.USER_ACCOUNT_ERROR);
        }
        // 生成token
        String tokenData = business.getId() + "-" + RoleEnum.BUSINESS.name();
        String token = TokenUtils.createToken(tokenData, business.getPassword());
        business.setToken(token);
        return business;
    }

    /**
     * 注册
     */
    public void register(Account account) {
        Business business = new Business();
        BeanUtils.copyProperties(account, business);
        add(business);
    }

    /**
     * 修改密码
     */
    public void updatePassword(Account account) {
        Business business = businessMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(business)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!account.getPassword().equals(business.getPassword())) {
            throw new CustomException(ResultCodeEnum.PARAM_PASSWORD_ERROR);
        }
        business.setPassword(account.getNewPassword());
        businessMapper.updateById(business);
    }

    /**
     * 校验商家权限
     */
    public void checkBusiness() {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.BUSINESS.name().equals(currentUser.getRole())) {
            Business business = selectById(currentUser.getId());
            if (!"通过".equals(business.getStatus())) {
                throw new CustomException(ResultCodeEnum.NO_AUTH);
            }
        }
    }

    /**
     * 包装商家数据
     */
    public void wrapBusiness(Business business) {
        List<Comment> commentList = commentService.selectByBusinessId(business.getId());
        double sum = commentList.stream().map(Comment::getStar).reduce(Double::sum).orElse(0D) + 5D;
        business.setStar(BigDecimal.valueOf(sum).divide(BigDecimal.valueOf(commentList.size() + 1), 1, RoundingMode.UP).doubleValue());
        List<Orders> ordersList = ordersService.selectByBusinessId(business.getId());
        int saleCount = 0;
        for (Orders o : ordersList) {
            OrdersItem ordersItem = new OrdersItem();
            ordersItem.setOrderId(o.getId());
            List<OrdersItem> ordersItems = ordersItemService.selectAll(ordersItem);
            saleCount += ordersItems.stream().map(OrdersItem::getNum).reduce(Integer::sum).orElse(0);
        }
        business.setSaleCount(saleCount);
    }


    /**
     * 查询首页推荐商家（最多展示8条）
     *      1、第一标准根据用户浏览，
     *      2、第二标准根据用户收藏商家推荐
     *      3、随机补全 满足8个商家
     */
    public List<Business> getHomeBusiness() {

        // 获取用户信息
        Account currentUser = TokenUtils.getCurrentUser();
        Integer id = currentUser.getId();
        List<Business> list = new ArrayList<>();

        // 1、第一标准根据用户浏览
        List<Business> browsingList = browsingService.getListBrowsingByUserId(id);

        List<Integer> businessIdList = new ArrayList<>();
        if(browsingList != null){
            for (Business business : browsingList) {
                businessIdList.add(business.getId());
            }
            list.addAll(browsingList);
        }
        // 2、第二标准根据用户收藏商家推荐
        List<Business> collectList =  collectService.getListBrowsingByUserId(id, businessIdList);
        if(collectList != null){
            for (Business collect : collectList) {
                wrapBusiness(collect);
                businessIdList.add(collect.getId());
            }
            list.addAll(collectList);
        }

        // 3、随机补全 满足8个商家
        if(list.size() < 8){
            List<Business> businessList =  businessMapper.getListBrowsingNotIds(businessIdList);
            if(businessList != null){
                int size = 8 -  list.size();
                if(businessList.size() <= size){
                    for (Business business : businessList) {
                        wrapBusiness(business);
                    }
                    list.addAll(businessList);
                }else{
                    List<Business> businessLists = businessList.subList(0, size);
                    for (Business business : businessLists) {
                        wrapBusiness(business);
                    }
                    list.addAll(businessLists);
                }
            }
        }
        return list;
    }
}
