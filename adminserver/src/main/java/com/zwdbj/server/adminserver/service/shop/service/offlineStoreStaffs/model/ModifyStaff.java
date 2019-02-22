package com.zwdbj.server.adminserver.service.shop.service.offlineStoreStaffs.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@ApiModel(description = "修改员工信息")
public class ModifyStaff {
    @NotBlank(message = "姓名不能为空")
    @ApiModelProperty(value = "姓名")
    String fullName;
    @Length(min = 11, max = 11)
    @ApiModelProperty(value = "手机号")
    String phone;
    @ApiModelProperty(value = "员工id")
    long userId;
    @ApiModelProperty(value = "认证资质 暂时没有 预留")
    String qualification;
    @ApiModelProperty(value = "备注 ")
    String notes;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}