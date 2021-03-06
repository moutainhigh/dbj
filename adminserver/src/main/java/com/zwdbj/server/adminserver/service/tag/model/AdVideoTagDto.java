package com.zwdbj.server.adminserver.service.tag.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "视频标签输出")
public class AdVideoTagDto {
    @ApiModelProperty(value = "id")
    private long id;
    @ApiModelProperty(value = "标签名称")
    String name;
    @ApiModelProperty(value = "标签的状态,0:正常,1:停用")
    int isDeleted;
    @ApiModelProperty(value = "标签使用次数")
    Long resNumber;
    @ApiModelProperty(value = "标签描述")
    String desc;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getResNumber() {
        return resNumber;
    }

    public void setResNumber(Long resNumber) {
        this.resNumber = resNumber*43;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }
}
