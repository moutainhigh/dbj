package com.zwdbj.server.adminserver.service.shop.service.offlineStoreStaffs.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel(description = " 门店员工，或者代言人")
public class OfflineStoreStaffs {
    @ApiModelProperty(value = "员工姓名")
    String fullName;
    @ApiModelProperty(value = "租户id")
    long tenantId;
    @ApiModelProperty(value = "联系电话")
    String phone;
    @ApiModelProperty(value = "用户id")
    long id;
    /// <summary>
    /// 是否为代言人
    /// </summary>
    /// <value><c>true</c> if is super star; otherwise, <c>false</c>.</value>

    @ApiModelProperty(value = "是否成为代言人")
    boolean isSuperStar;
    @ApiModelProperty(value = "认证资质")
    String qualification;
    @ApiModelProperty(value = "添加时间")
    Date createTime;
    @ApiModelProperty(value = "备注")
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

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }


    public long getTenantId() {
        return tenantId;
    }

    public void setTenantId(long tenantId) {
        this.tenantId = tenantId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isSuperStar() {
        return isSuperStar;
    }

    public void setSuperStar(boolean superStar) {
        isSuperStar = superStar;
    }
}
