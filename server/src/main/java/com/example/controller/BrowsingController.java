package com.example.controller;

import com.example.common.Result;
import com.example.entity.Account;
import com.example.entity.Browsing;
import com.example.entity.Business;
import com.example.service.BrowsingService;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 浏览商家记录表前端操作接口
 **/
@RestController
@RequestMapping("/browsing")
public class BrowsingController {

    @Resource
    private BrowsingService browsingService;



    /**
     * 查询浏览历史
     */
    @GetMapping("/selectAll")
    public Result selectAll(Integer userId){
        List<Business> list = browsingService.getListBrowsingByUserId(userId);
        return Result.success(list);
    }

}
