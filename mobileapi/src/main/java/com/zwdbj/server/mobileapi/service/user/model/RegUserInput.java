package com.zwdbj.server.mobileapi.service.user.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(description = "注册账号")
public class RegUserInput extends NewMyPasswordInput implements Serializable {
    @ApiModelProperty(value = "此手机号是否被注册，100:未被注册，201:已经注册过但是没有设置密码,202:已经注册过并设置密码")
    private int type;
    @ApiModelProperty(value = "谁推荐用户注册,若无则为0")
    private long recommendUserId;

    public long getRecommendUserId() {
        return recommendUserId;
    }

    public void setRecommendUserId(long recommendUserId) {
        this.recommendUserId = recommendUserId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
