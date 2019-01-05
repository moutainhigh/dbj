package com.zwdbj.server.adminserver.service.shop.service.shopdetail.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "店铺位置信息")
public class LocationInfo {

    @ApiModelProperty(value = "经度")
    float longitude;
    @ApiModelProperty(value = "纬度")
    float latitude;
    @ApiModelProperty(value = "详细地址")
    String address;
    @ApiModelProperty(value = "城市")
    int cityId;
    @ApiModelProperty(value = "cityLevel")
    String cityLevel;



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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCityLevel() {
        return cityLevel;
    }

    public void setCityLevel(String cityLevel) {
        this.cityLevel = cityLevel;
    }
}
