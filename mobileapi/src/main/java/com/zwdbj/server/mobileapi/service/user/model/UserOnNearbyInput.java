package com.zwdbj.server.mobileapi.service.user.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "查询同城用户的请求字段")
public class UserOnNearbyInput {
    @ApiModelProperty(value = "经度")
    private float longitude;
    @ApiModelProperty(value = "纬度")
    private float latitude;

    @ApiModelProperty(value = "距离 单位:米")
    private int distance;

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
