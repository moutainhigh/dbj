package com.zwdbj.server.adminserver.controller.shop;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zwdbj.server.adminserver.service.shop.service.productBrands.model.ProductBrands;
import com.zwdbj.server.adminserver.service.shop.service.productBrands.service.ProductBrandsService;
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

@RestController
@RequestMapping(value = "/api/shop/productBrands/dbj")
@Api(produces = "品牌相关")
public class ProductBrandsController {
    @Autowired
    private ProductBrandsService productBrandsServiceImpl;

    @RequiresAuthentication
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ApiOperation(value = "创建品牌")
    public ResponseData<Long> create(@RequestBody ProductBrands productBrands) {
        ServiceStatusInfo<Long> serviceStatusInfo = productBrandsServiceImpl.createProductBrands(productBrands);
        if (serviceStatusInfo.isSuccess()) {
            return new ResponseData(ResponseDataCode.STATUS_NORMAL, "", serviceStatusInfo.getData());
        }
        return new ResponseData(ResponseDataCode.STATUS_ERROR, serviceStatusInfo.getMsg(), null);
    }

    @RequiresAuthentication
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "删除品牌")
    public ResponseData<Long> delete(@PathVariable Long id) {
        ServiceStatusInfo<Long> serviceStatusInfo = productBrandsServiceImpl.deleteById(id);
        if (serviceStatusInfo.isSuccess()) {
            return new ResponseData(ResponseDataCode.STATUS_NORMAL, "", serviceStatusInfo.getData());
        }
        return new ResponseData(ResponseDataCode.STATUS_ERROR, serviceStatusInfo.getMsg(), null);
    }

    @RequiresAuthentication
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation(value = "修改品牌")
    public ResponseData<Long> update(@RequestBody ProductBrands productBrands) {
        ServiceStatusInfo<Long> serviceStatusInfo = productBrandsServiceImpl.updateProductBrands(productBrands);
        if (serviceStatusInfo.isSuccess()) {
            return new ResponseData(ResponseDataCode.STATUS_NORMAL, "", serviceStatusInfo.getData());
        }
        return new ResponseData(ResponseDataCode.STATUS_ERROR, serviceStatusInfo.getMsg(), null);
    }

    @RequiresAuthentication
    @RequestMapping(value = "/select", method = RequestMethod.GET)
    @ApiOperation(value = "查询品牌")
    public ResponsePageInfoData<List<ProductBrands>> select(@RequestParam(value = "pageNo", required = true, defaultValue = "1") int pageNo,
                                                            @RequestParam(value = "rows", required = true, defaultValue = "30") int rows) {
        PageHelper.startPage(pageNo, rows);
        List<ProductBrands> list = productBrandsServiceImpl.selectAll().getData();
        PageInfo<ProductBrands> pageInfo = new PageInfo(list);
        return new ResponsePageInfoData(ResponseDataCode.STATUS_NORMAL, "", list, pageInfo.getTotal());

    }
}
