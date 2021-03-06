package com.zwdbj.server.adminserver.service.shop.service.shoppingCart.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "修改购物车")
public class ProductCartModifyInput {
    @ApiModelProperty(value = "买家id")
    long userId;
    @ApiModelProperty(value = "卖家店铺id")
    long sellerId;
    @ApiModelProperty(value = "商品id")
    long productId;
    @ApiModelProperty(value = "商品SKU的id")
    long productskuId;
    @ApiModelProperty(value = "购买的商品数量")
    long item;
    @ApiModelProperty(value = "商品单价")
    float price;
    @ApiModelProperty(value = "ip")
    String ip;
    @ApiModelProperty(value = "ua")
    String ua;
    @ApiModelProperty(value = "过期时间")
    long expireTime;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getSellerId() {
        return sellerId;
    }

    public void setSellerId(long sellerId) {
        this.sellerId = sellerId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getProductskuId() {
        return productskuId;
    }

    public void setProductskuId(long productskuId) {
        this.productskuId = productskuId;
    }

    public long getItem() {
        return item;
    }

    public void setItem(long item) {
        this.item = item;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUa() {
        return ua;
    }

    public void setUa(String ua) {
        this.ua = ua;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }
}
