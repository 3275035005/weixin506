package com.example.controller;

import com.example.common.Result;
import com.example.entity.Account;
import com.example.entity.Browsing;
import com.example.entity.Business;
import com.example.service.BrowsingService;
import com.example.service.BusinessService;
import com.example.service.CollectService;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 商家表前端操作接口
 **/
@RestController
@RequestMapping("/business")
public class BusinessController {

    @Resource
    private BusinessService businessService;

    @Resource
    private BrowsingService browsingService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Business business) {
        businessService.add(business);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        businessService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        businessService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Business business) {
        businessService.updateById(business);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {

        Account currentUser = TokenUtils.getCurrentUser();
        // 新增浏览记录
        Browsing browsing = new Browsing();
        browsing.setBusinessId(id);
        browsing.setUserId(currentUser.getId());
        browsingService.add(browsing);

        Business business = businessService.selectById(id);
        return Result.success(business);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Business business) {
        List<Business> list = businessService.selectAll(business);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Business business,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Business> page = businessService.selectPage(business, pageNum, pageSize);
        return Result.success(page);
    }

    /**
     * 查询首页推荐商家（最多展示8条）
     *      1、第一标准根据用户浏览，
     *      2、第二标准根据用户收藏商家推荐
     *      3、随机补全 满足8个商家
     */
    @GetMapping("/getHomeBusiness")
    public Result getHomeBusiness(){
        List<Business> list = businessService.getHomeBusiness();
        return Result.success(list);
    }

}
