package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.common.enums.OrderStatusEnum;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.*;
import com.example.exception.CustomException;
import com.example.mapper.CommentMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 评价表业务处理
 **/
@Service
public class CommentService {

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private BusinessService businessService;

    @Resource
    private GoodsService goodsService;

    @Resource
    private UserService userService;

    @Resource
    private OrdersService ordersService;

    /**
     * 新增
     */
    @Transactional
    public void add(Comment comment) {
        try {
            comment.setTime(DateUtil.now());
            commentMapper.insert(comment);
            ordersService.updateStatus(OrderStatusEnum.DONE.getValue(), comment.getOrderId());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(ResultCodeEnum.DATA_REPEAT_ERROR);
        }
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        commentMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            commentMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Comment comment) {
        commentMapper.updateById(comment);
    }

    /**
     * 根据ID查询
     */
    public Comment selectById(Integer id) {
        return commentMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Comment> selectAll(Comment comment) {
        List<Comment> comments = commentMapper.selectAll(comment);
        for (Comment c : comments) {
            wrapComment(c);
        }
        return comments;
    }

    /**
     * 分页查询
     */
    public PageInfo<Comment> selectPage(Comment comment, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.BUSINESS.name().equals(currentUser.getRole())) {
            comment.setBusinessId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Comment> list = selectAll(comment);
        for (Comment c : list) {
            Business business = businessService.selectById(c.getBusinessId());
            c.setBusiness(business);
            User user = userService.selectById(c.getUserId());
            c.setUser(user);
        }
        return PageInfo.of(list);
    }

    public List<Comment> selectByBusinessId(Integer businessId) {
        return commentMapper.selectByBusinessId(businessId);
    }

    private void wrapComment(Comment comment) {
        comment.setBusiness(businessService.selectById(comment.getBusinessId()));
        comment.setGoods(goodsService.selectById(comment.getGoodsId()));
        comment.setUser(userService.selectById(comment.getUserId()));
    }
}