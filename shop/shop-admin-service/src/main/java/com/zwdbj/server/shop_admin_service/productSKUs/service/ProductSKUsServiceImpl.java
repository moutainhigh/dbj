package com.zwdbj.server.shop_admin_service.productSKUs.service;

import com.zwdbj.server.shop_admin_service.productSKUs.mapper.IProductSKUsMapper;
import com.zwdbj.server.shop_admin_service.productSKUs.model.ProductSKUs;
import com.zwdbj.server.utility.common.UniqueIDCreater;
import com.zwdbj.server.utility.model.ServiceStatusInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class ProductSKUsServiceImpl implements ProductSKUsService {
    @Resource
    private IProductSKUsMapper productSKUsMapper;


    @Override
    public ServiceStatusInfo<Long> createProductSKUs(ProductSKUs productSKUs) {
        Long id = UniqueIDCreater.generateID();
        Long result=0L;
        try {
            result=this.productSKUsMapper.createProductSKUs(id,productSKUs);
            return  new ServiceStatusInfo<Long>(0,"",result);
        }
        catch (Exception e){
            return  new ServiceStatusInfo<Long>(1,"创建商品SKU失败"+e.getMessage(),result);
        }
    }

    @Override
    public ServiceStatusInfo<Long> deleteById(Long id) {
        Long result = 0L;
        try {
            result = this.productSKUsMapper.deleteById(id);
            return new ServiceStatusInfo<Long>(0, "", result);
        } catch (Exception e) {
            return new ServiceStatusInfo<Long>(1, "删除商品SKU失败"+e.getMessage(), result);
        }
    }
    @Override
    public ServiceStatusInfo<Long> updateProductSKUs(ProductSKUs productSKUs) {
        Long result = 0L;
        try {
            result = this.productSKUsMapper.updateProductSKUs(productSKUs);
            return new ServiceStatusInfo<Long>(0, "", result);
        } catch (Exception e) {
            return new ServiceStatusInfo<Long>(1, "修改商品SKU失败"+e.getMessage(), result);
        }
    }

    @Override
    public ServiceStatusInfo<List<ProductSKUs>> selectAll() {
       List<ProductSKUs> productSKUsList=null;
        try {
            productSKUsList = this.productSKUsMapper.selectAll();
            return new ServiceStatusInfo<List<ProductSKUs>>(0, "", productSKUsList);
        } catch (Exception e) {
            return new ServiceStatusInfo<List<ProductSKUs>>(1, "查询所有商品SKU失败"+e.getMessage(), productSKUsList);
        }
    }
}
