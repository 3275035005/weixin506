package com.example.service;
import cn.hutool.core.date.DateUtil;
import com.example.entity.*;
import com.example.mapper.BrowsingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 浏览记录表业务处理
 **/
@Service
public class BrowsingService {

    @Autowired
    private BrowsingMapper businessMapper;

    @Resource
    private BusinessService businessService;
    /**
     * 新增
     */
    public void add(Browsing business) {
        business.setTime(DateUtil.now());
        businessMapper.insert(business);
    }

    /**
     * 查询用户浏览记录
     * @param id
     * @return
     */
    public List<Business> getListBrowsingByUserId(Integer id){
        List<Business> list = businessMapper.getListBrowsingByUserId(id);
        for (Business business : list) {
            businessService.wrapBusiness(business);
        }
        return list;
    }

}
