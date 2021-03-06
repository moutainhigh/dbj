package com.zwdbj.server.adminserver.service.shop.service.shopdetail.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(value = "营业时间")
public class OpeningHours implements Serializable {

    private static final long serialVersionUID = -1697023955748317504L;
    @ApiModelProperty(value = "开门时间")
    int openTime;
    @ApiModelProperty(value = "打烊时间")
    int closeTime;
    @ApiModelProperty(value = "星期几")
    int day;
    @ApiModelProperty(value = "店铺id")
    long storeId;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof OpeningHours) {
            if (((OpeningHours) obj).getOpenTime() == this.getOpenTime() && ((OpeningHours) obj).closeTime == this.getCloseTime()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.getOpenTime() + this.getCloseTime() * 17;
    }

    public int getOpenTime() {
        return openTime;
    }

    public void setOpenTime(int openTime) {
        this.openTime = openTime;
    }

    public int getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(int closeTime) {
        this.closeTime = closeTime;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }
}
