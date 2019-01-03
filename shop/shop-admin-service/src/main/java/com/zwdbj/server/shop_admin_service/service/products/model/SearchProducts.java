package com.zwdbj.server.shop_admin_service.service.products.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "搜索需要的商品信息")
public class SearchProducts {
    @ApiModelProperty(value = "id")
    Long id;
    @ApiModelProperty(value = "商品类型")
    Long productType;
    @ApiModelProperty(value = "商品编码")
    String numberId;
    @ApiModelProperty(value = "商品名称")
    String name;
    @ApiModelProperty(value = "销量上限")
    long salesUp;
    @ApiModelProperty(value = "销量下限")
    long salseDown;
    @ApiModelProperty(value = "商品价格上限")
    float priceUp;
    @ApiModelProperty(value = "商品价格下限")
    float priceDown;
    @ApiModelProperty(value = "商品分组")
    long productGroupId;

    public Long getProductType() {
        return productType;
    }

    public void setProductType(Long productType) {
        this.productType = productType;
    }

    public String getNumberId() {
        return numberId;
    }

    public void setNumberId(String numberId) {
        this.numberId = numberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSalesUp() {
        return salesUp;
    }

    public void setSalesUp(long salesUp) {
        this.salesUp = salesUp;
    }

    public long getSalseDown() {
        return salseDown;
    }

    public void setSalseDown(long salseDown) {
        this.salseDown = salseDown;
    }

    public float getPriceUp() {
        return priceUp;
    }

    public void setPriceUp(float priceUp) {
        this.priceUp = priceUp;
    }

    public float getPriceDown() {
        return priceDown;
    }

    public void setPriceDown(float priceDown) {
        this.priceDown = priceDown;
    }


    public long getProductGroupId() {
        return productGroupId;
    }

    public void setProductGroupId(long productGroupId) {
        this.productGroupId = productGroupId;
    }
}