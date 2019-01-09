package com.zwdbj.server.mobileapi.service.wxMiniProgram.product.mapper;

import com.zwdbj.server.mobileapi.service.wxMiniProgram.product.model.ProductInput;
import com.zwdbj.server.mobileapi.service.wxMiniProgram.product.model.ProductOut;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface IProductMapper {

    /**
     * 查询小程序的兑换商城 目前默认storeId=1
     * @return
     */
    @SelectProvider(type = ProductSqlProvider.class,method = "seleteList")
    List<ProductOut> selectWXXCXShopProduct(@Param("productInput") ProductInput productInput);

    /**
     * 根据id查询数据
     * @param id
     * @return
     */
    @Select("SELECT " +
            "id," +
            "productType," +
            "productDetailType," +
            "`name`," +
            "categoryId," +
            "brandId," +
            "inventory," +
            "imageUrls," +
            "limitPerPerson " +
            "from shop_products " +
            "where publish=1 and isDeleted=0 and storeId=1 and id=#{id}")
    ProductOut selectWXXCXById(long id);

    @Update("update shop_productSKUs set inventory=inventory-#{num},sales=sales+#{num} where id=#{id}")
    int updateProductSkuNum( @Param("id") long productSkuId, @Param("num") int num);
    @Update("update shop_products set inventory=inventory-#{num},sales=sales+#{num} where id=#{id}")
    int updateProductNum(@Param("id")long productId,@Param("num")int num);

    @Select("select inventory from shop_productSKUs where id=#{id}")
    long getProductSkuInventory(@Param("id")long productSkuId);
    @Select("select inventory from shop_products where id=#{id}")
    long getProductInventory(@Param("id")long productId);
}
