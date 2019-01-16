package com.zwdbj.server.mobileapi.service.wxMiniProgram.productSKUs.mapper;

import com.zwdbj.server.mobileapi.service.wxMiniProgram.productSKUs.model.ProductSKUs;
import org.apache.ibatis.annotations.*;


@Mapper
public interface IProductSKUsMapper {

    @Select("select * from shop_productskus where id=#{id} and isDeleted=0 ")
    ProductSKUs selectById(@Param("id") Long id);

    @Select("select * from shop_productskus where productId=#{productId}")
    ProductSKUs selectByProductId(@Param("productId") Long productId);
}