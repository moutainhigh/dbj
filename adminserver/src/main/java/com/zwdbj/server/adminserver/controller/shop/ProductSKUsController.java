package com.zwdbj.server.adminserver.controller.shop;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zwdbj.server.adminserver.service.shop.service.productSKUs.model.ProductSKUs;
import com.zwdbj.server.adminserver.service.shop.service.productSKUs.service.ProductSKUsService;
import com.zwdbj.server.adminserver.service.shop.service.products.model.Products;
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
@RequestMapping(value = "/api/shop/productSKUs/dbj")
@Api("商品SKU相关")
public class ProductSKUsController {
    @Autowired
    private ProductSKUsService productSKUsServiceImpl;

    @RequiresAuthentication
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ApiOperation(value = "创建商品SKU")
    public ResponseData<Long> create(@RequestBody ProductSKUs productSKUs) {
        ServiceStatusInfo<Long> serviceStatusInfo = productSKUsServiceImpl.createProductSKUs(productSKUs);
        if (serviceStatusInfo.isSuccess()) {
            return new ResponseData(ResponseDataCode.STATUS_NORMAL, "", serviceStatusInfo.getData());
        }
        return new ResponseData(ResponseDataCode.STATUS_ERROR, serviceStatusInfo.getMsg(), null);

    }

    @RequiresAuthentication
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation(value = "修改商品SKU")
    public ResponseData<Long> update(@RequestBody ProductSKUs productSKUs) {
        ServiceStatusInfo<Long> serviceStatusInfo = productSKUsServiceImpl.updateProductSKUs(productSKUs);
        if (serviceStatusInfo.isSuccess()) {
            return new ResponseData(ResponseDataCode.STATUS_NORMAL, "", serviceStatusInfo.getData());
        }
        return new ResponseData(ResponseDataCode.STATUS_ERROR, serviceStatusInfo.getMsg(), null);

    }

    @RequiresAuthentication
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "删除商品SKU")
    public ResponseData<Long> deleteById(@PathVariable Long id) {
        ServiceStatusInfo<Long> serviceStatusInfo = productSKUsServiceImpl.deleteById(id);
        if (serviceStatusInfo.isSuccess()) {
            return new ResponseData(ResponseDataCode.STATUS_NORMAL, "", serviceStatusInfo.getData());
        }
        return new ResponseData(ResponseDataCode.STATUS_ERROR, serviceStatusInfo.getMsg(), null);

    }

    @RequiresAuthentication
    @RequestMapping(value = "/select", method = RequestMethod.GET)
    @ApiOperation(value = "查询所有商品SKU")
    public ResponsePageInfoData<List<ProductSKUs>> selectAll(@RequestParam(value = "pageNo", required = true, defaultValue = "1") int pageNo,
                                                             @RequestParam(value = "rows", required = true, defaultValue = "30") int rows) {

        PageHelper.startPage(pageNo, rows);
        List<ProductSKUs> productSKUsList = productSKUsServiceImpl.selectAll().getData();
        PageInfo<Products> pageInfo = new PageInfo(productSKUsList);
        return new ResponsePageInfoData(ResponseDataCode.STATUS_NORMAL, "", productSKUsList, pageInfo.getTotal());

    }

    @RequiresAuthentication
    @GetMapping(value = "/select/{id}")
    @ApiOperation(value = "查询单个商品的SKU")
    public ResponseData<ProductSKUs> selectById(@PathVariable Long id){
        ServiceStatusInfo<ProductSKUs> serviceStatusInfo = productSKUsServiceImpl.selectById(id);
        if(serviceStatusInfo.isSuccess()){
            return new ResponseData(ResponseDataCode.STATUS_NORMAL,"",serviceStatusInfo.getData());
        }
        return new ResponseData(ResponseDataCode.STATUS_ERROR,serviceStatusInfo.getMsg(),null);
    }

}
