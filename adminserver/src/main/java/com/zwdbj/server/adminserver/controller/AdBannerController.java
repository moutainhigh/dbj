package com.zwdbj.server.adminserver.controller;

import com.zwdbj.server.adminserver.identity.RoleIdentity;
import com.zwdbj.server.adminserver.service.adBanner.model.AdBannerDto;
import com.zwdbj.server.adminserver.service.adBanner.model.AdBannerInfo;
import com.zwdbj.server.adminserver.service.adBanner.model.AdBannerSerchInput;
import com.zwdbj.server.adminserver.service.adBanner.service.AdBannerService;
import com.zwdbj.server.basemodel.model.ResponseData;
import com.zwdbj.server.basemodel.model.ServiceStatusInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(description = "adBanner相关")
@RequestMapping("/api/banner")
@RestController
public class AdBannerController {
    @Autowired
    private AdBannerService adBannerServiceImpl;


    @RequiresAuthentication
    @RequiresRoles(value = {RoleIdentity.ADMIN_ROLE, RoleIdentity.MARKET_ROLE}, logical = Logical.OR)
    @ApiOperation(value = "获取Banners")
    @RequestMapping(value = "/searchAllBanners", method = RequestMethod.GET)
    public ResponseData<List<AdBannerInfo>> searchAllAdBanners() {
        ServiceStatusInfo<List<AdBannerInfo>> statusInfo = this.adBannerServiceImpl.searchAllAdBanners();
        if (statusInfo.isSuccess()) {
            return new ResponseData<>(0, "", statusInfo.getData());
        }
        return new ResponseData<>(1, statusInfo.getMsg(), null);
    }

    @RequiresAuthentication
    @RequiresRoles(value = {RoleIdentity.ADMIN_ROLE, RoleIdentity.MARKET_ROLE}, logical = Logical.OR)
    @ApiOperation(value = "搜索Banners")
    @RequestMapping(value = "/searchBanners", method = RequestMethod.POST)
    public ResponseData<List<AdBannerInfo>> searchAdBanners(@RequestBody AdBannerSerchInput input) {
        ServiceStatusInfo<List<AdBannerInfo>> statusInfo = this.adBannerServiceImpl.searchAdBanners(input);
        if (statusInfo.isSuccess()) {
            return new ResponseData<>(0, "", statusInfo.getData());
        }
        return new ResponseData<>(1, statusInfo.getMsg(), null);
    }

    @RequiresAuthentication
    @RequiresRoles(value = {RoleIdentity.ADMIN_ROLE, RoleIdentity.MARKET_ROLE}, logical = Logical.OR)
    @ApiOperation(value = "修改adBanners")
    @RequestMapping(value = "/modifyBanners/{id}", method = RequestMethod.POST)
    public ResponseData<Long> modifyAdBanners(@RequestBody AdBannerDto dto, @PathVariable("id") long id) {
        ServiceStatusInfo<Long> statusInfo = this.adBannerServiceImpl.modifyAdBanner(dto, id);
        if (statusInfo.isSuccess()) {
            return new ResponseData<>(0, "", statusInfo.getData());
        }
        return new ResponseData<>(1, statusInfo.getMsg(), null);
    }

    @RequiresAuthentication
    @RequiresRoles(value = {RoleIdentity.ADMIN_ROLE, RoleIdentity.MARKET_ROLE}, logical = Logical.OR)
    @ApiOperation(value = "创建Banners")
    @RequestMapping(value = "/createBanners", method = RequestMethod.POST)
    public ResponseData<Long> createAdBanners(@RequestBody AdBannerDto dto) {
        ServiceStatusInfo<Long> statusInfo = this.adBannerServiceImpl.createAdBanner(dto);
        if (statusInfo.isSuccess()) {
            return new ResponseData<>(0, "", statusInfo.getData());
        }
        return new ResponseData<>(1, statusInfo.getMsg(), null);
    }

    @RequiresAuthentication
    @RequiresRoles(value = {RoleIdentity.ADMIN_ROLE, RoleIdentity.MARKET_ROLE}, logical = Logical.OR)
    @ApiOperation(value = "删除Banners")
    @RequestMapping(value = "/deleteBanners/{id}", method = RequestMethod.GET)
    public ResponseData<Long> deleteAdBanners(@PathVariable("id") long id) {
        ServiceStatusInfo<Long> statusInfo = this.adBannerServiceImpl.deleteAdBanner(id);
        if (statusInfo.isSuccess()) {
            return new ResponseData<>(0, "", statusInfo.getData());
        }
        return new ResponseData<>(1, statusInfo.getMsg(), null);
    }
}
