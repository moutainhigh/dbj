package com.zwdbj.server.adminserver.service.shop.service.shopdetail.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(value = "优惠券")
public class DiscountCoupon implements Serializable {
    private static final long serialVersionUID = 4252646167682535073L;
    @ApiModelProperty(value = "优惠券id")
    long id;
    @ApiModelProperty(value = "name")
    String name;
    @ApiModelProperty(value = "店铺id")
    long storeId;
    @ApiModelProperty(value = "发放数量")
    int couponCount;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    public int getCouponCount() {
        return couponCount;
    }

    public void setCouponCount(int couponCount) {
        this.couponCount = couponCount;
    }
}
