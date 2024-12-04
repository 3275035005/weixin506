package com.example.mapper;

import com.example.entity.Orders;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 操作orders相关数据接口
*/
public interface OrdersMapper {

    /**
      * 新增
    */
    int insert(Orders orders);

    /**
      * 删除
    */
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(Orders orders);

    /**
      * 根据ID查询
    */
    Orders selectById(Integer id);

    /**
      * 查询所有
    */
    List<Orders> selectAll(Orders orders);

    @Delete("update orders set status = '已取消' where business_id = #{ businessId }")
    void updateByBusinessId(Integer businessId);

    @Select("select * from orders where business_id = #{businessId} and (status = '待评价' or status = '已完成')")
    List<Orders> selectByBusinessId(Integer businessId);

    @Update("update orders set status = #{status} where id = #{orderId}")
    void updateStatus(@Param("status") String status, @Param("orderId") Integer orderId);

    @Select("select * from orders where status = '待评价' or status = '已完成'")
    List<Orders> selectDoneOrders();

}