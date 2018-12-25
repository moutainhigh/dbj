package com.zwdbj.server.adminserver.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zwdbj.server.adminserver.identity.RoleIdentity;
import com.zwdbj.server.utility.model.ResponseData;
import com.zwdbj.server.utility.model.ResponseDataCode;
import com.zwdbj.server.utility.model.ResponsePageInfoData;
import com.zwdbj.server.utility.model.ServiceStatusInfo;
import com.zwdbj.server.adminserver.service.category.model.*;
import com.zwdbj.server.adminserver.service.category.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(description = "分类")
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @ApiOperation("分类列表")
    @RequestMapping(value = "/search",method = RequestMethod.POST)
    public ResponseData<List<CategoryDto>> search(@RequestBody CategorySearchInput input) {
        List<CategoryDto> dtos = this.categoryService.search(input);
        return new ResponseData<>(ResponseDataCode.STATUS_NORMAL,"",dtos);
    }

    @RequiresAuthentication
    @RequestMapping(value = "/dbj/basicCategory",method = RequestMethod.POST)
    @ApiOperation("基本分类信息列表")
    @RequiresRoles(RoleIdentity.ADMIN_ROLE)
    public ResponsePageInfoData<List<AdBasicCategoryDto>>  basicCompalinAd(@RequestBody AdBasicCategoryInput input,
                                                                           @RequestParam(value = "pageNo",required = true,defaultValue = "1") int pageNo,
                                                                           @RequestParam(value = "rows",required = true,defaultValue = "13") int rows){
        Page<AdBasicCategoryDto> pageInfo = PageHelper.startPage(pageNo,rows);
        List<AdBasicCategoryDto> categoryDtos = this.categoryService.basicCompalinAd(input);
        return new ResponsePageInfoData<>(ResponseDataCode.STATUS_NORMAL,"",categoryDtos,pageInfo.getTotal());
    }

    @RequiresAuthentication
    @RequestMapping(value = {"/dbj/basicCategory/add","/dbj/basicCategory/add/{id}"},method = RequestMethod.POST)
    @ApiOperation("新建基本分类信息列表")
    @RequiresRoles(RoleIdentity.ADMIN_ROLE)
    public ResponseData<Long> addCategoryAd(@PathVariable(required = false)Long id,
                                            @RequestBody AdNewCategoryInput input){
        ServiceStatusInfo<Long> statusInfo = this.categoryService.addCategoryAd(id,input);

        if (statusInfo.isSuccess()){
            return new ResponseData<>(ResponseDataCode.STATUS_NORMAL,"",statusInfo.getData());
        }
        return new ResponseData<>(ResponseDataCode.STATUS_ERROR,statusInfo.getMsg(),null);
    }

    @RequiresAuthentication
    @RequestMapping(value = "/dbj/basicCategory/edit/{id}",method = RequestMethod.POST)
    @ApiOperation("修改分类的名字")
    @RequiresRoles(RoleIdentity.ADMIN_ROLE)
    public ResponseData<Long> editCategoryAd(@PathVariable Long id,
                                             @RequestBody AdNewCategoryNameInput input){
        ServiceStatusInfo<Long> statusInfo = this.categoryService.editCategoryAd(id,input);
        if (statusInfo.isSuccess()){
            return new ResponseData<>(ResponseDataCode.STATUS_NORMAL,"",statusInfo.getData());
        }
        return new ResponseData<>(ResponseDataCode.STATUS_ERROR,statusInfo.getMsg(),null);
    }

    @RequiresAuthentication
    @RequestMapping(value = "/dbj/categoryDetails/{id}",method = RequestMethod.GET)
    @ApiOperation("查看分类的品种")
    @RequiresRoles(RoleIdentity.ADMIN_ROLE)
    public ResponsePageInfoData<List<AdBasicCategoryDto>> categoryDetailsAd(@PathVariable Long id,
                                                                            @RequestParam(value = "pageNo",required = true,defaultValue = "1") int pageNo,
                                                                            @RequestParam(value = "rows",required = true,defaultValue = "13") int rows){
        Page<AdBasicCategoryDto> pageInfo = PageHelper.startPage(pageNo,rows);
        List<AdBasicCategoryDto> categoryDtos = this.categoryService.categoryDetailsAd(id);
        return new ResponsePageInfoData<>(ResponseDataCode.STATUS_NORMAL,"",categoryDtos,pageInfo.getTotal());
    }

    @RequiresAuthentication
    @RequestMapping(value = "/dbj/delCategory/{id}",method = RequestMethod.GET)
    @ApiOperation("删除分类的品种")
    @RequiresRoles(RoleIdentity.ADMIN_ROLE)
    public ResponseData<Long> delCategoryAd(@PathVariable Long id){
        ServiceStatusInfo<Long> statusInfo = this.categoryService.delCategoryAd(id);
        if (statusInfo.isSuccess()){
            return new ResponseData<>(ResponseDataCode.STATUS_NORMAL,"",statusInfo.getData());
        }
        return new ResponseData<>(ResponseDataCode.STATUS_ERROR,statusInfo.getMsg(),null);
    }

}
