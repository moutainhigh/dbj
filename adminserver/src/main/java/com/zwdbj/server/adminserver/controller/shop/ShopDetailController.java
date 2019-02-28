package com.zwdbj.server.adminserver.controller.shop;

import com.zwdbj.server.adminserver.service.category.model.StoreServiceCategory;
import com.zwdbj.server.adminserver.service.shop.service.shopdetail.model.*;
import com.zwdbj.server.adminserver.service.shop.service.shopdetail.service.ShopDetailService;
import com.zwdbj.server.tokencenter.TokenCenterManager;
import com.zwdbj.server.tokencenter.model.AuthUser;
import com.zwdbj.server.utility.common.shiro.JWTUtil;
import com.zwdbj.server.basemodel.model.ResponseData;
import com.zwdbj.server.basemodel.model.ServiceStatusInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api("店铺信息相关")
@RequestMapping("/api/shop/storeInfo/dbj")
@RestController
public class ShopDetailController {
    @Autowired
    private ShopDetailService shopDetailServiceImpl;
    @Autowired
    private TokenCenterManager tokenCenterManager;

    @RequestMapping(value = "/basicInfo", method = RequestMethod.GET)
    @ApiOperation(value = "展示店铺基本信息")
    public ResponseData<StoreDto> basicInfo() {
        long userId = JWTUtil.getCurrentId();

        ServiceStatusInfo<AuthUser> statusInfo1 = tokenCenterManager.fetchUser(String.valueOf(userId));
        AuthUser authUser = statusInfo1.getData();
        long legalSubjectId = authUser.getLegalSubjectId();
        // long legalSubjectId = tokenCenterManager.fetchUser(String.valueOf(userId)).getData().getLegalSubjectId();
        ServiceStatusInfo<StoreDto> statusInfo = shopDetailServiceImpl.findStoreDetail(legalSubjectId);
        if (statusInfo.isSuccess()) {
            return new ResponseData<>(0, "", statusInfo.getData());

        }
        return new ResponseData<>(1, statusInfo.getMsg(), null);


    }

    @RequestMapping(value = "/openingHours", method = RequestMethod.GET)
    @ApiOperation(value = "展示营业时间")
    public ResponseData<List<OpeningHours>> openingHours() {
        long userId = JWTUtil.getCurrentId();

        long legalSubjectId = tokenCenterManager.fetchUser(String.valueOf(userId)).getData().getLegalSubjectId();
        ServiceStatusInfo<List<OpeningHours>> statusInfo = shopDetailServiceImpl.findOpeningHours(legalSubjectId);
        if (statusInfo.isSuccess()) {
            return new ResponseData<>(0, "", statusInfo.getData());

        }
        return new ResponseData<>(1, statusInfo.getMsg(), null);


    }

    @RequestMapping(value = "/modifyOpeningHours/{storeId}", method = RequestMethod.POST)
    @ApiOperation(value = "修改营业时间")
    public ResponseData<Long> modifyOpeningHours(@RequestBody List<OpeningHours> list, @PathVariable("storeId") long storeId) {
        long userId = JWTUtil.getCurrentId();

        long legalSubjectId = tokenCenterManager.fetchUser(String.valueOf(userId)).getData().getLegalSubjectId();
        ServiceStatusInfo<Long> statusInfo = this.shopDetailServiceImpl.modifyOpeningHours(list, storeId, legalSubjectId);
        if (statusInfo.isSuccess()) {
            return new ResponseData<>(0, "", statusInfo.getData());
        }
        return new ResponseData<>(1, statusInfo.getMsg(), null);

    }

    @RequestMapping(value = "/addOpeningHours/{storeId}", method = RequestMethod.POST)
    @ApiOperation(value = "增加营业时间段")
    public ResponseData<Long> addOpeningHours(@RequestBody List<OpeningHours> list, @PathVariable("storeId") long storeId) {

        ServiceStatusInfo<Long> statusInfo = this.shopDetailServiceImpl.addOpeningHours(list, storeId);
        if (statusInfo.isSuccess()) {
            return new ResponseData<>(0, "", statusInfo.getData());
        }
        return new ResponseData<>(1, statusInfo.getMsg(), null);


    }

    @RequestMapping(value = "/location", method = RequestMethod.GET)
    @ApiOperation(value = "显示位置信息")
    public ResponseData<LocationInfo> showLocation() {
        long userId = JWTUtil.getCurrentId();

        long legalSubjectId = tokenCenterManager.fetchUser(String.valueOf(userId)).getData().getLegalSubjectId();
        ServiceStatusInfo<LocationInfo> statusInfo = this.shopDetailServiceImpl.showLocation(legalSubjectId);
        if (statusInfo.isSuccess()) {
            return new ResponseData<>(0, "", statusInfo.getData());
        }
        return new ResponseData<>(1, statusInfo.getMsg(), null);

    }

    @RequestMapping(value = "/modifylocation/{storeId}", method = RequestMethod.POST)
    @ApiOperation(value = "修改位置信息")
    public ResponseData<Long> modifylocation(@RequestBody LocationInfo info, @PathVariable("storeId") long storeId) {

        ServiceStatusInfo<Long> statusInfo = this.shopDetailServiceImpl.modifylocation(info, storeId);
        if (statusInfo.isSuccess()) {
            return new ResponseData<>(0, "", statusInfo.getData());

        }
        return new ResponseData<>(1, statusInfo.getMsg(), null);

    }


    @RequestMapping(value = "/extraService", method = RequestMethod.GET)
    @ApiOperation(value = "展示店铺额外服务范围")
    public ResponseData<List<StoreServiceCategory>> showExtraServices() {

        long userId = JWTUtil.getCurrentId();

        long legalSubjectId = tokenCenterManager.fetchUser(String.valueOf(userId)).getData().getLegalSubjectId();
        ServiceStatusInfo<List<StoreServiceCategory>> statusInfo = this.shopDetailServiceImpl.findExtraService(legalSubjectId);
        if (statusInfo.isSuccess()) {
            return new ResponseData<>(0, "", statusInfo.getData());
        }
        return new ResponseData<>(1, statusInfo.getMsg(), null);
    }

    @RequestMapping(value = "/modifyExtraService/{storeId}", method = RequestMethod.POST)
    @ApiOperation(value = "修改店铺额外服务范围")
    public ResponseData<Long> modifyExtraService(@RequestBody List<StoreServiceCategory> storeServiceCategory, @PathVariable("storeId") long storeId) {
        long userId = JWTUtil.getCurrentId();

        long legalSubjectId = tokenCenterManager.fetchUser(String.valueOf(userId)).getData().getLegalSubjectId();
        ServiceStatusInfo<Long> statusInfo = this.shopDetailServiceImpl.modifyExtraService(storeId, legalSubjectId, storeServiceCategory);
        if (statusInfo.isSuccess()) {
            return new ResponseData<>(0, "", statusInfo.getData());
        }
        return new ResponseData<>(1, statusInfo.getMsg(), null);
    }

    @RequestMapping(value = "/searchAllExtraService", method = RequestMethod.GET)
    @ApiOperation(value = "查询所有额外服务范围")
    public ResponseData<List<StoreServiceCategory>> searchAllExtraService() {
        ServiceStatusInfo<List<StoreServiceCategory>> statusInfo = this.shopDetailServiceImpl.findExtraService(1L);
        if (statusInfo.isSuccess()) {
            return new ResponseData<>(0, "", statusInfo.getData());
        }
        return new ResponseData<>(1, statusInfo.getMsg(), null);
    }

    @RequestMapping(value = "/serviceScopes", method = RequestMethod.GET)
    @ApiOperation(value = "展示店铺服务范围")
    public ResponseData<List<StoreServiceCategory>> showServiceScopes() {
        long userId = JWTUtil.getCurrentId();

        long legalSubjectId = tokenCenterManager.fetchUser(String.valueOf(userId)).getData().getLegalSubjectId();
        ServiceStatusInfo<List<StoreServiceCategory>> statusInfo = this.shopDetailServiceImpl.findServiceScope(legalSubjectId);
        if (statusInfo.isSuccess()) {
            return new ResponseData<>(0, "", statusInfo.getData());
        }
        return new ResponseData<>(1, statusInfo.getMsg(), null);
    }

    @RequestMapping(value = "/modifyServiceScopes/{storeId}", method = RequestMethod.POST)
    @ApiOperation(value = "修改店铺额外服务范围")
    public ResponseData<Long> modifyServiceScopes(@RequestBody List<StoreServiceCategory> storeServiceCategory, @PathVariable("storeId") long storeId) {
        long userId = JWTUtil.getCurrentId();

        long legalSubjectId = tokenCenterManager.fetchUser(String.valueOf(userId)).getData().getLegalSubjectId();
        ServiceStatusInfo<Long> statusInfo = this.shopDetailServiceImpl.modifyServiceScopes(storeId, legalSubjectId, storeServiceCategory);
        if (statusInfo.isSuccess()) {
            return new ResponseData<>(0, "", statusInfo.getData());
        }
        return new ResponseData<>(1, statusInfo.getMsg(), null);
    }

    @RequestMapping(value = "/uploadCheck", method = RequestMethod.POST)
    @ApiOperation("上传资料审核")
    public ResponseData<Long> uploadCheck(@RequestBody QualificationInput input) {
        long userId = JWTUtil.getCurrentId();

        long legalSubjectId = tokenCenterManager.fetchUser(String.valueOf(userId)).getData().getLegalSubjectId();
        ServiceStatusInfo<Long> statusInfo = this.shopDetailServiceImpl.uploadCheck(input, legalSubjectId);

        if (statusInfo.isSuccess()) {
            return new ResponseData<>(0, "", statusInfo.getData());
        }
        return new ResponseData<>(1, statusInfo.getMsg(), null);
    }

    @RequestMapping(value = "/modifyStoreImage", method = RequestMethod.POST)
    @ApiOperation(value = "修改店铺照片")
    public ResponseData<Long> modifyStoreImage(@RequestBody StoreImage storeImage) {
//        long userId = JWTUtil.getCurrentId();
//
//        long legalSubjectId = tokenCenterManager.fetchUser(String.valueOf(userId)).getData().getLegalSubjectId();
        ServiceStatusInfo<Long> statusInfo = this.shopDetailServiceImpl.modifyStoreImage(storeImage, 1);
        if (statusInfo.isSuccess()) {
            return new ResponseData<>(0, "", statusInfo.getData());
        }
        return new ResponseData<>(1, statusInfo.getMsg(), null);
    }
}
