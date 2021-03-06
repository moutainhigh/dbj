package com.zwdbj.server.adminserver.service.shop.service.products.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zwdbj.server.adminserver.service.shop.service.products.common.ProductDetailType;
import com.zwdbj.server.adminserver.service.shop.service.products.common.ValidType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

@ApiModel(value = "新增的商品对象")
public class CreateProducts {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "商品类型O:实物产品1:虚拟商品")
    @NotNull(message = "商品类型不能为空")
    private Long productType;

    @ApiModelProperty(value = "产品详细类型 DELIVERY: 实物产品 NODELIVERY:虚拟商品(不需要物流) CARD:卡券(服务中套餐) CASHCOUPON:代金券")
    @NotNull(message = "产品详细类型不能为空")
    private ProductDetailType productDetailType;

    @ApiModelProperty(value = "商品名称")
    @NotNull(message = "商品名称不能为空")
    private String name;

    @ApiModelProperty(value = "店铺ID")
    private long storeId;

    @ApiModelProperty(value = "库存 库存 -10000不限库存")
    private long inventory;

    @ApiModelProperty(value = "是否上架")
    private boolean publish;

    @ApiModelProperty(value = "上架时间")
    private long specifyPublishTime;

    @ApiModelProperty(value = "商品详情")
    private String detailDescription;

    @ApiModelProperty(value = "是否限购 0：表示不限购 大于0数字表示没人只能买商品的数量")
    private int limitPerPerson;

    @ApiModelProperty(value = "原价")
    private long originalPrice;

    @NotNull
    @Min(value = 1,message = "促销价最少为0.01")
    @ApiModelProperty(value = "促销价")
    private Long promotionPrice;

    @ApiModelProperty(value = "节假日是否可用")
    private boolean festivalCanUse;

    @ApiModelProperty(value = "PAY_SPEC_HOUR_VALIDED:付款后指定小时生效")
    private int specHoursValid;

    @ApiModelProperty(value = "生效后多少天内有效")
    private int validDays;

    @ApiModelProperty(value = "生效后指定时间范围内生效")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date validStartTime;

    @ApiModelProperty(value = "生效后指定时间范围内生效")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date validEndTime;

    @ApiModelProperty(value = "生效类型 PAY_VALIDED:付款后立即生效 PAY_NEXTDAY_VALIDED:付款后次日生效 PAY_SPEC_HOUR_VALIDED:付款后指定小时生效")
    private ValidType validType;

    @ApiModelProperty(value = "支持金币兑换购买")
    private boolean supportCoin;

    @ApiModelProperty(value = "品牌ID")
    private Long brandId;

    @ApiModelProperty(value = "种类ID")
    private Long categoryId;

    @ApiModelProperty(value = "商品图片地址")
    private String imageUrls;

    @ApiModelProperty(value = "规则说明")
    private String ruleDescription;

    @ApiModelProperty(value = "预约信息")
    private String appointment;

    @ApiModelProperty(value = "是否与其他优惠券共用")
    private boolean stackUse;

    public String getAppointment() {
        return appointment;
    }

    public void setAppointment(String appointment) {
        this.appointment = appointment;
    }

    public boolean isStackUse() {
        return stackUse;
    }

    public void setStackUse(boolean stackUse) {
        this.stackUse = stackUse;
    }

    public String getRuleDescription() {
        return ruleDescription;
    }

    public void setRuleDescription(String ruleDescription) {
        this.ruleDescription = ruleDescription;
    }

    public String getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(String imageUrls) {
        this.imageUrls = imageUrls;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public boolean isSupportCoin() {
        return supportCoin;
    }

    public void setSupportCoin(boolean supportCoin) {
        this.supportCoin = supportCoin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    public Long getProductType() {
        return productType;
    }

    public void setProductType(Long productType) {
        this.productType = productType;
    }

    public ProductDetailType getProductDetailType() {
        return productDetailType;
    }

    public void setProductDetailType(ProductDetailType productDetailType) {
        this.productDetailType = productDetailType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getInventory() {
        return inventory;
    }

    public void setInventory(long inventory) {
        this.inventory = inventory;
    }

    public boolean isPublish() {
        return publish;
    }

    public void setPublish(boolean publish) {
        this.publish = publish;
    }

    public long getSpecifyPublishTime() {
        return specifyPublishTime;
    }

    public void setSpecifyPublishTime(long specifyPublishTime) {
        this.specifyPublishTime = specifyPublishTime;
    }

    public String getDetailDescription() {
        return detailDescription;
    }

    public void setDetailDescription(String detailDescription) {
        this.detailDescription = detailDescription;
    }

    public int getLimitPerPerson() {
        return limitPerPerson;
    }

    public void setLimitPerPerson(int limitPerPerson) {
        this.limitPerPerson = limitPerPerson;
    }

    public long getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(long originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Long getPromotionPrice() {
        return promotionPrice;
    }

    public void setPromotionPrice(Long promotionPrice) {
        this.promotionPrice = promotionPrice;
    }

    public boolean isFestivalCanUse() {
        return festivalCanUse;
    }

    public void setFestivalCanUse(boolean festivalCanUse) {
        this.festivalCanUse = festivalCanUse;
    }

    public int getSpecHoursValid() {
        return specHoursValid;
    }

    public void setSpecHoursValid(int specHoursValid) {
        this.specHoursValid = specHoursValid;
    }

    public int getValidDays() {
        return validDays;
    }

    public void setValidDays(int validDays) {
        this.validDays = validDays;
    }

    public Date getValidStartTime() {
        return validStartTime;
    }

    public void setValidStartTime(Date validStartTime) {
        this.validStartTime = validStartTime;
    }

    public Date getValidEndTime() {
        return validEndTime;
    }

    public void setValidEndTime(Date validEndTime) {
        this.validEndTime = validEndTime;
    }

    public ValidType getValidType() {
        return validType;
    }

    public void setValidType(ValidType validType) {
        this.validType = validType;
    }
}
