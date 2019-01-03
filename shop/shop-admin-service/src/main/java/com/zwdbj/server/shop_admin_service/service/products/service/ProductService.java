package com.zwdbj.server.shop_admin_service.service.products.service;


import com.zwdbj.server.shop_admin_service.service.products.model.Products;
import com.zwdbj.server.shop_admin_service.service.products.model.SearchProducts;
import com.zwdbj.server.utility.model.ServiceStatusInfo;

import java.util.List;

public interface ProductService {
    ServiceStatusInfo<Long> createProducts(Products products);

    ServiceStatusInfo<Long> deleteProductsById(Long id);

    ServiceStatusInfo<Long> updateProducts(Products products);

    ServiceStatusInfo<List<Products>> selectAll();
    ServiceStatusInfo<List<Products>> searchProducts(SearchProducts searchProduct);

    /**
     * 批量商品上下架
     * @param id
     * @param publish
     * @return
     */
    ServiceStatusInfo<Long> updatePublishs(Long[] id , boolean publish);

    /**
     * 查询单个商品
     * @param id
     * @return
     */
    ServiceStatusInfo<Products> selectById(long id);

    /**
     * 批量删除商品
     * @param id
     * @return
     */
    ServiceStatusInfo<Long> deleteByProducts(Long[] id);
}
