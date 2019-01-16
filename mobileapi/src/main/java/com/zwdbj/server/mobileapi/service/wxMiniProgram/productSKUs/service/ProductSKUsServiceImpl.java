package com.zwdbj.server.mobileapi.service.wxMiniProgram.productSKUs.service;

import com.zwdbj.server.mobileapi.service.wxMiniProgram.productSKUs.mapper.IProductSKUsMapper;
import com.zwdbj.server.mobileapi.service.wxMiniProgram.productSKUs.model.ProductSKUs;
import com.zwdbj.server.utility.model.ServiceStatusInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class ProductSKUsServiceImpl implements ProductSKUsService {
    @Resource
    private IProductSKUsMapper productSKUsMapper;


    @Override
    public ServiceStatusInfo<ProductSKUs> selectById(Long id) {
        try{
            ProductSKUs productSKUs = this.productSKUsMapper.selectById(id);
            return new ServiceStatusInfo<>(0, "", productSKUs);
        }catch(Exception e){
            return new ServiceStatusInfo<>(1, "查询单个商品SKU失败"+e.getMessage(), null);
        }
    }
}