package com.zwdbj.server.adminserver.service.shop.service.staff.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "员工信息")
public class StaffInfo {
    @ApiModelProperty(value = "员工姓名")
    String name;


}
