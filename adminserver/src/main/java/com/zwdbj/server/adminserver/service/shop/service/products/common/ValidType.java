package com.zwdbj.server.adminserver.service.shop.service.products.common;

public enum ValidType {

    /**
     * 无
     */
    NONE(""),
    /**
     * 付款后立即生效
     */
    PAY_VALIDED("PAY_VALIDED"),
    /**
     * 付款后次日生效
     */
    PAY_NEXTDAY_VALIDED("PAY_NEXTDAY_VALIDED"),
    /**
     * 付款后指定小时生效
     */
    PAY_SPEC_HOUR_VALIDED("PAY_SPEC_HOUR_VALIDED");

    String name;

    ValidType(String name){
        this.name = name;
    }

}
