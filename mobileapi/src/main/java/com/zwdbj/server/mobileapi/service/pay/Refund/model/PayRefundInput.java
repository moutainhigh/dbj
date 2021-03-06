package com.zwdbj.server.mobileapi.service.pay.Refund.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(description = "申请退款的请求参数")
public class PayRefundInput implements Serializable {
    private static final long serialVersionUID = 8927411700074233813L;
    @ApiModelProperty(value = "订单id")
    private long orderId;
    @ApiModelProperty("退款的原因说明")
    private String refundReason;

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }



    public String getRefundReason() {
        return refundReason;
    }

    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }
}
