package com.example.entity;

import java.io.Serializable;

/**
 * 浏览记录
*/
public class Browsing implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 序号 */
    private Integer id;

    /** 商家ID */
    private Integer businessId;

    /** 用户ID */
    private Integer userId;

    /** 浏览时间 */
    private String time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
