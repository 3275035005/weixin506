package com.example.mapper;

import com.example.entity.Browsing;
import com.example.entity.Business;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 操作browsing相关数据接口
*/
public interface BrowsingMapper {

    /**
     * 新增
    */
    int insert(Browsing browsing);

    /**
     * 根据用户id查询浏览商家
     */
    List<Business> getListBrowsingByUserId(@Param("id") Integer id);


}
