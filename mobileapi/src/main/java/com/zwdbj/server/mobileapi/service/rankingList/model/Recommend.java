package com.zwdbj.server.mobileapi.service.rankingList.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "推荐关注")
public class Recommend {
    @ApiModelProperty(value = "用户id")
    long userId;
    @ApiModelProperty(value = "用户名")
    String nickName;
    @ApiModelProperty(value = "用户头像url")
    String avatarUrl;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
