package com.zwdbj.server.adminserver.service.video.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "搜索视频")
public class SearchVideoAdInput {
    @ApiModelProperty(value = "视频状态，-1：所有，0：正常的。其他的值非正常")
    int status;
    @ApiModelProperty(value = "关联的商品，-1：所有，其他值为真正关联的商品数")
    int isLinkProduct;
    @ApiModelProperty(value = "视频标签，-1：所有，0：没有标签1：有标签")
    int isHaveTag;
    @ApiModelProperty(value = "关键字，可以查询标题")
    String keywords;
    @ApiModelProperty(value = "上传者的角色名称:admin market finance normal null")
    String roleName;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIsLinkProduct() {
        return isLinkProduct;
    }

    public void setIsLinkProduct(int isLinkProduct) {
        this.isLinkProduct = isLinkProduct;
    }

    public int getIsHaveTag() {
        return isHaveTag;
    }

    public void setIsHaveTag(int isHaveTag) {
        this.isHaveTag = isHaveTag;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
}
