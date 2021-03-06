package com.zwdbj.server.adminserver.service.userAssets.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "分类型存储用户小饼干总额")
public class UserCoinType {
    @ApiModelProperty(value = "id")
    Long id;
    @ApiModelProperty(value = "用户小饼干获得类型")
    String type;
    @ApiModelProperty(value = "小饼干总额")
    Long coins;
    @ApiModelProperty(value = "用户id")
    Long userId;
    @ApiModelProperty(value = "用户冻结的小饼干")
    long lockedCoins;

    public long getLockedCoins() {
        return lockedCoins;
    }

    public void setLockedCoins(long lockedCoins) {
        this.lockedCoins = lockedCoins;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getCoins() {
        return coins;
    }

    public void setCoins(Long coins) {
        this.coins = coins;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
