package com.zwdbj.server.adminserver.controller.shop;

import com.github.pagehelper.PageInfo;
import com.zwdbj.server.adminserver.service.shop.service.products.model.*;
import com.zwdbj.server.adminserver.service.shop.service.products.service.ProductService;
import com.zwdbj.server.basemodel.model.ResponseData;
import com.zwdbj.server.basemodel.model.ResponseDataCode;
import com.zwdbj.server.basemodel.model.ResponsePageInfoData;
import com.zwdbj.server.basemodel.model.ServiceStatusInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/shop/products")
@Api(description = "商品相关")
public class ProductsController {
    @Autowired
    private ProductService productServiceImpl;

    @RequiresAuthentication
    @RequestMapping(value = "/onSale", method = RequestMethod.GET)
    @ApiOperation(value = "查询销售中商品")
    public ResponsePageInfoData<List<Products>> findAllProducts(@RequestParam(value = "pageNo", required = true, defaultValue = "1") int pageNo,
                                                                @RequestParam(value = "rows", required = true, defaultValue = "30") int rows,
                                                                SearchProducts searchProduct) {
        ServiceStatusInfo<List<Products>> serviceStatusInfo = this.productServiceImpl.searchCondition(searchProduct,1,pageNo,rows);
        if(!serviceStatusInfo.isSuccess()){
            return new ResponsePageInfoData(ResponseDataCode.STATUS_ERROR, serviceStatusInfo.getMsg(), null, 0L);
        }
        List<Products> productsList = serviceStatusInfo.getData();
        PageInfo<Products> pageInfo = new PageInfo(productsList);
        return new ResponsePageInfoData(ResponseDataCode.STATUS_NORMAL, serviceStatusInfo.getMsg(), productsList, pageInfo.getTotal());
    }

    @RequiresAuthentication
    @GetMapping(value = "/onSellOut")
    @ApiOperation(value = "查询售完商品")
    public ResponsePageInfoData<List<Products>> findSellOut(@RequestParam(value = "pageNo", required = true, defaultValue = "1") int pageNo,
                                                            @RequestParam(value = "rows", required = true, defaultValue = "30") int rows,
                                                            SearchProducts searchProduct){
        ServiceStatusInfo<List<Products>> serviceStatusInfo = this.productServiceImpl.searchCondition(new SearchProducts(),2,pageNo,rows);
        if(!serviceStatusInfo.isSuccess()){
            return new ResponsePageInfoData(ResponseDataCode.STATUS_ERROR, serviceStatusInfo.getMsg(), null, 0L);
        }
        List<Products> productsList = serviceStatusInfo.getData();
        PageInfo<Products> pageInfo = new PageInfo(productsList);
        return new ResponsePageInfoData(ResponseDataCode.STATUS_NORMAL, serviceStatusInfo.getMsg(), productsList, pageInfo.getTotal());
    }

    @RequiresAuthentication
    @GetMapping(value = "/onToStayOn")
    @ApiOperation(value = "查询待上架商品")
    public ResponsePageInfoData<List<Products>> findToStayOn(@RequestParam(value = "pageNo", required = true, defaultValue = "1") int pageNo,
                                                            @RequestParam(value = "rows", required = true, defaultValue = "30") int rows,
                                                            SearchProducts searchProduct){
        ServiceStatusInfo<List<Products>> serviceStatusInfo = this.productServiceImpl.searchCondition(searchProduct,3,pageNo,rows);
        if(!serviceStatusInfo.isSuccess()){
            return new ResponsePageInfoData(ResponseDataCode.STATUS_ERROR, serviceStatusInfo.getMsg(), null, 0L);
        }
        List<Products> productsList = serviceStatusInfo.getData();
        PageInfo<Products> pageInfo = new PageInfo(productsList);
        return new ResponsePageInfoData(ResponseDataCode.STATUS_NORMAL, serviceStatusInfo.getMsg(), productsList, pageInfo.getTotal());
    }

    @RequiresAuthentication
    @GetMapping(value = "/onAll")
    @ApiOperation(value = "查询所有商品 type=0全部 1销售中 2已售完 3待上架")
    public ResponsePageInfoData<List<Products>> findOnAll(@RequestParam(value = "pageNo", required = true, defaultValue = "1") int pageNo,
                                                             @RequestParam(value = "rows", required = true, defaultValue = "30") int rows,
                                                             SearchProducts searchProduct,int type){
        ServiceStatusInfo<List<Products>> serviceStatusInfo = this.productServiceImpl.searchCondition(searchProduct,type,pageNo,rows);
        if(!serviceStatusInfo.isSuccess()){
            return new ResponsePageInfoData(ResponseDataCode.STATUS_ERROR, serviceStatusInfo.getMsg(), null, 0L);
        }
        List<Products> productsList = serviceStatusInfo.getData();
        PageInfo<Products> pageInfo = new PageInfo(productsList);
        return new ResponsePageInfoData(ResponseDataCode.STATUS_NORMAL, serviceStatusInfo.getMsg(), productsList, pageInfo.getTotal());
    }

    @RequiresAuthentication
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ApiOperation(value = "创建商品")
    public ResponseData<Long> createProducts(@RequestBody @Valid CreateProducts createProducts) {
        ServiceStatusInfo<Long> serviceStatusInfo = this.productServiceImpl.createProducts(createProducts);
        if (serviceStatusInfo.isSuccess()) {
            return new ResponseData(ResponseDataCode.STATUS_NORMAL, "", serviceStatusInfo.getData());
        }
        return new ResponseData(ResponseDataCode.STATUS_ERROR, serviceStatusInfo.getMsg(), null);
    }

    @RequiresAuthentication
    @ApiOperation(value = "修改商品")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseData<Long> updateProducts(@RequestBody UpdateProducts updateProducts) {
        ServiceStatusInfo<Long> serviceStatusInfo = this.productServiceImpl.updateProducts(updateProducts);
        if (serviceStatusInfo.isSuccess()) {
            return new ResponseData(ResponseDataCode.STATUS_NORMAL, "", serviceStatusInfo.getData());
        }
        return new ResponseData(ResponseDataCode.STATUS_ERROR, serviceStatusInfo.getMsg(), null);
    }

    @RequiresAuthentication
    @PostMapping(value = "/updatePublishs")
    @ApiOperation(value = "批量商品上下架")
    public ResponseData<Long> updatePublishs(@RequestBody PublishsProducts publishsProducts){
        ServiceStatusInfo<Long> serviceStatusInfo = this.productServiceImpl.updatePublishs(publishsProducts.getId(),publishsProducts.isPublish());
        if(serviceStatusInfo.isSuccess()){
            return new ResponseData<>(ResponseDataCode.STATUS_NORMAL, "", serviceStatusInfo.getData());
        }
        return new ResponseData<>(ResponseDataCode.STATUS_ERROR, serviceStatusInfo.getMsg(), null);
    }

    @RequiresAuthentication
    @GetMapping(value = "/find/{id}")
    @ApiOperation(value = "查询单个商品")
    public ResponseData<ProductsOut> findById(@PathVariable long id){
        ServiceStatusInfo<ProductsOut> serviceStatusInfo = this.productServiceImpl.selectById(id);
        if(serviceStatusInfo.isSuccess()){
            return new ResponseData<>(ResponseDataCode.STATUS_NORMAL, "", serviceStatusInfo.getData());
        }
        return new ResponseData<>(ResponseDataCode.STATUS_ERROR, serviceStatusInfo.getMsg() , null);
    }

    @RequiresAuthentication
    @PostMapping(value = "/deleteByProducts")
    @ApiOperation(value = "批量删除商品")
    public ResponseData<Long> deleteByProducts(@RequestBody BasicsProducts basicsProducts){
        ServiceStatusInfo<Long> serviceStatusInfo = this.productServiceImpl.deleteByProducts(basicsProducts.getId());
        if(serviceStatusInfo.isSuccess()){
            return new ResponseData<>(ResponseDataCode.STATUS_NORMAL, "", serviceStatusInfo.getData());
        }
        return new ResponseData<>(ResponseDataCode.STATUS_ERROR, serviceStatusInfo.getMsg() , null);
    }

}