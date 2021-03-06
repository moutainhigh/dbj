package com.zwdbj.server.adminserver.controller.shop;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zwdbj.server.adminserver.service.shop.service.offlineStoreExtraServices.model.OfflineStoreExtraServices;
import com.zwdbj.server.adminserver.service.shop.service.offlineStoreExtraServices.service.OfflineStoreExtraServicesService;
import com.zwdbj.server.basemodel.model.ResponseData;
import com.zwdbj.server.basemodel.model.ResponseDataCode;
import com.zwdbj.server.basemodel.model.ResponsePageInfoData;
import com.zwdbj.server.basemodel.model.ServiceStatusInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(value = "/api/shop/offlineStoreExtraServices/dbj")
@RestController
@Api(description = "门店其他服务相关")
public class OfflineStoreExtraServivesController {
    @Autowired
    private OfflineStoreExtraServicesService offlineStoreExtraServicesServiceImpl;

    @RequiresAuthentication
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ApiOperation(value = "添加门店其他服务")
    public ResponseData<Long> create(@RequestBody OfflineStoreExtraServices offlineStoreExtraServices) {
        ServiceStatusInfo<Long> serviceStatusInfo = offlineStoreExtraServicesServiceImpl.create(offlineStoreExtraServices);
        if (serviceStatusInfo.isSuccess()) {
            return new ResponseData<>(ResponseDataCode.STATUS_NORMAL, "", serviceStatusInfo.getData());
        }
        return new ResponseData<>(ResponseDataCode.STATUS_ERROR, serviceStatusInfo.getMsg(), null);
    }

    @RequiresAuthentication
    @RequestMapping(value = "/delete/{extraServiceId}/{storeId}", method = RequestMethod.GET)
    @ApiOperation(value = "删除门店其他服务")
    public ResponseData<Long> deleteById(@PathVariable("extraServiceId") Long extraServiceId, @PathVariable("storeId") long storeId) {
        ServiceStatusInfo<Long> serviceStatusInfo = offlineStoreExtraServicesServiceImpl.deleteById(extraServiceId, storeId);
        if (serviceStatusInfo.isSuccess()) {
            return new ResponseData<>(ResponseDataCode.STATUS_NORMAL, "", serviceStatusInfo.getData());
        }
        return new ResponseData<>(ResponseDataCode.STATUS_ERROR, serviceStatusInfo.getMsg(), null);
    }

    @RequiresAuthentication
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation(value = "修改门店其他服务")
    public ResponseData<Long> update(@RequestBody OfflineStoreExtraServices offlineStoreExtraServices) {
        ServiceStatusInfo<Long> serviceStatusInfo = offlineStoreExtraServicesServiceImpl.update(offlineStoreExtraServices);
        if (serviceStatusInfo.isSuccess()) {
            return new ResponseData<>(ResponseDataCode.STATUS_NORMAL, "", serviceStatusInfo.getData());
        }
        return new ResponseData<>(ResponseDataCode.STATUS_NORMAL, serviceStatusInfo.getMsg(), null);
    }

    @RequiresAuthentication
    @RequestMapping(value = "/select/{offlineStoreId}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id查询门店其他服务")
    public ResponseData<List<OfflineStoreExtraServices>> selectById(@PathVariable("offlineStoreId") Long offlineStoreId) {
        ServiceStatusInfo<List<OfflineStoreExtraServices>> serviceStatusInfo = offlineStoreExtraServicesServiceImpl.selectByofflineStoreId(offlineStoreId);
        if (serviceStatusInfo.isSuccess()) {
            return new ResponseData<>(ResponseDataCode.STATUS_NORMAL, "", serviceStatusInfo.getData());
        }
        return new ResponseData<>(ResponseDataCode.STATUS_NORMAL, serviceStatusInfo.getMsg(), null);
    }

    @RequiresAuthentication
    @RequestMapping(value = "/select", method = RequestMethod.GET)
    @ApiOperation(value = "查询所有门店其他服务")
    public ResponsePageInfoData<List<OfflineStoreExtraServices>> select(@RequestParam(value = "pageNo", defaultValue = "1", required = true) int pageNo,
                                                                        @RequestParam(value = "rows", defaultValue = "30", required = true) int rows) {

        PageHelper.startPage(pageNo, rows);
        List<OfflineStoreExtraServices> list = offlineStoreExtraServicesServiceImpl.select().getData();
        PageInfo<OfflineStoreExtraServices> pageInfo = new PageInfo<>(list);

        return new ResponsePageInfoData<>(ResponseDataCode.STATUS_NORMAL, "", list, pageInfo.getTotal());

    }

}
