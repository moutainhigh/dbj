package com.zwdbj.server.adminserver.service.shop.service.products.service;


import com.github.pagehelper.PageHelper;
import com.zwdbj.server.adminserver.config.MainKeyType;
import com.zwdbj.server.adminserver.service.shop.service.productCard.mapper.IProductCardMapper;
import com.zwdbj.server.adminserver.service.shop.service.productCard.model.ProductCard;
import com.zwdbj.server.adminserver.service.shop.service.productCashCoupon.mapper.IProductCashCouponMapper;
import com.zwdbj.server.adminserver.service.shop.service.productCashCoupon.model.ProductCashCoupon;
import com.zwdbj.server.adminserver.service.shop.service.productSKUs.mapper.IProductSKUsMapper;
import com.zwdbj.server.adminserver.service.shop.service.productSKUs.model.ProductSKUs;
import com.zwdbj.server.adminserver.service.shop.service.products.common.ProductDetailType;
import com.zwdbj.server.adminserver.service.shop.service.products.common.ValidType;
import com.zwdbj.server.adminserver.service.shop.service.products.mapper.IProductsMapper;
import com.zwdbj.server.adminserver.service.shop.service.products.model.*;
import com.zwdbj.server.adminserver.service.shop.service.store.service.StoreService;
import com.zwdbj.server.tokencenter.TokenCenterManager;
import com.zwdbj.server.tokencenter.model.AuthUser;
import com.zwdbj.server.utility.common.UniqueIDCreater;
import com.zwdbj.server.utility.common.shiro.JWTUtil;
import com.zwdbj.server.basemodel.model.ServiceStatusInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    @Resource
    protected IProductsMapper iProductMapper;

    @Resource
    protected IProductSKUsMapper iProductSKUsMapper;

    @Resource
    protected IProductCardMapper iProductCardMapper;

    @Resource
    protected IProductCashCouponMapper iProductCashCouponMapper;

    @Autowired
    protected TokenCenterManager tokenCenterManager;

    @Autowired
    private StoreService storeServiceImpl;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public ServiceStatusInfo<Long> createProducts(CreateProducts createProducts) {
        ServiceStatusInfo serviceStatusInfo = judgeStoreId();
        if (!serviceStatusInfo.isSuccess()) {
            return serviceStatusInfo;
        }
        createProducts.setStoreId((long) serviceStatusInfo.getData());
        serviceStatusInfo = judgeProducts(createProducts);
        if (serviceStatusInfo != null) {
            return serviceStatusInfo;
        }
        //生成唯一id
        long id = UniqueIDCreater.generateID();
        long result = this.iProductMapper.createProducts(id, createProducts);
        if (result == 0) {
            return new ServiceStatusInfo<>(1, "新增失败", result);
        }
        ProductSKUs productSKUs = new ProductSKUs();
        long skuid = UniqueIDCreater.generateID();
        productSKUs.setProductId(id);
        productSKUs.setInventory(createProducts.getInventory());
        productSKUs.setOriginalPrice(createProducts.getOriginalPrice());
        productSKUs.setPromotionPrice(createProducts.getPromotionPrice());
        result = iProductSKUsMapper.createProductSKUs(skuid, productSKUs);
        if(result == 0){
            throw new RuntimeException("SKU新增失败"+id);
        }
        if (ProductDetailType.CARD == createProducts.getProductDetailType()) {
            ProductCard productCard = new ProductCard(createProducts, id);
            productCard.setProductSKUId(skuid);
            result = this.iProductCardMapper.createProductCard(UniqueIDCreater.generateID(), productCard);
        }
        if (ProductDetailType.CASHCOUPON == createProducts.getProductDetailType()) {
            ProductCashCoupon productCashCoupon = new ProductCashCoupon(createProducts, id);
            productCashCoupon.setProductSKUId(skuid);
            result = this.iProductCashCouponMapper.createProductCashCoupon(UniqueIDCreater.generateID(), productCashCoupon);
        }
        if(result == 0){
            throw new RuntimeException("CARD或CASHCOUPON新增失败"+id);
        }
        redisTemplate.delete(MainKeyType.MAINPRODUCT);
        return new ServiceStatusInfo<>(0, "", result);
    }


    @Override
    public ServiceStatusInfo<Long> deleteProductsById(Long id) {
        Long result = 0L;
        ServiceStatusInfo serviceStatusInfo = judgeStoreId();
        if (!serviceStatusInfo.isSuccess()) {
            return serviceStatusInfo;
        }
        long storeId = (long) serviceStatusInfo.getData();
        try {
            result = this.iProductMapper.deleteProduct(id, storeId);
            redisTemplate.delete(MainKeyType.MAINPRODUCT);
            return new ServiceStatusInfo<>(0, "", result);
        } catch (Exception e) {
            return new ServiceStatusInfo<>(1, "删除失败" + e.getMessage(), result);
        }
    }

    @Override
    public ServiceStatusInfo<Long> updateProducts(UpdateProducts updateProducts) {
        ServiceStatusInfo serviceStatusInfo = judgeStoreId();
        if (!serviceStatusInfo.isSuccess()) {
            return serviceStatusInfo;
        }
        updateProducts.setStoreId((long) serviceStatusInfo.getData());
        Long result = 0L;
        result = this.iProductMapper.update(updateProducts);
        if (result == 0) {
            return new ServiceStatusInfo<>(1, "修改失败：影响行数" + result, null);
        }
        ProductSKUs productSKUs = new ProductSKUs();
        productSKUs.setProductId(updateProducts.getId());
        productSKUs.setInventory(updateProducts.getInventory());
        productSKUs.setOriginalPrice(updateProducts.getOriginalPrice());
        productSKUs.setPromotionPrice(updateProducts.getPromotionPrice());
        result = iProductSKUsMapper.updateProductSKUs(productSKUs);
        if(result == 0){
            throw new RuntimeException("SKU修改失败"+updateProducts.getId());
        }
        Products createProducts = iProductMapper.selectById(updateProducts.getId());
        if (ProductDetailType.CARD == createProducts.getProductDetailType()) {
            ProductCard productCard = new ProductCard(updateProducts, updateProducts.getId());
            result = this.iProductCardMapper.updateByProductIdByProductCard(productCard);
        }
        if (ProductDetailType.CASHCOUPON == createProducts.getProductDetailType()) {
            ProductCashCoupon productCashCoupon = new ProductCashCoupon(updateProducts, updateProducts.getId());
            result = this.iProductCashCouponMapper.updateByProductIdByProductCashCoupon(productCashCoupon);
        }
        if(result == 0){
            throw new RuntimeException("CARD或CASHCOUPON新增失败"+updateProducts.getId());
        }
        redisTemplate.delete(MainKeyType.MAINPRODUCT);
        return new ServiceStatusInfo<>(0, "", result);
    }
    @Override
    public ServiceStatusInfo<Integer> updateProductNum(long productId, long productSkuId, int num) {
        int result = this.iProductMapper.updateProductSkuNum(productSkuId, num);
        if (result == 0) return new ServiceStatusInfo<>(1, "商品数量更新失败", 0);
        result = this.iProductMapper.updateProductNum(productId, num);
        if (result == 0) return new ServiceStatusInfo<>(1, "商品数量更新失败", 0);
        return new ServiceStatusInfo<>(0, "", result);
    }


    @Override
    public ServiceStatusInfo<List<Products>> selectAll() {
        List<Products> result = null;
        try {
            result = this.iProductMapper.selectAll();
            return new ServiceStatusInfo<>(0, "", result);
        } catch (Exception e) {
            return new ServiceStatusInfo<>(1, "查询失败" + e.getMessage(), result);
        }
    }

    @Override
    public ServiceStatusInfo<List<Products>> searchProducts(SearchProducts searchProduct) {
        ServiceStatusInfo serviceStatusInfo = judgeStoreId();
        if (!serviceStatusInfo.isSuccess()) {
            return serviceStatusInfo;
        }
        searchProduct.setStoreId((long) serviceStatusInfo.getData());
        List<Products> result = null;
        try {
            result = this.iProductMapper.search(searchProduct);
            return new ServiceStatusInfo<>(0, "", result);
        } catch (Exception e) {
            return new ServiceStatusInfo<>(1, "搜索失败" + e.getMessage(), result);
        }
    }

    @Override
    public ServiceStatusInfo<Long> updatePublishs(Long[] id, boolean publish) {
        try {
            ServiceStatusInfo serviceStatusInfo = judgeStoreId();
            if (!serviceStatusInfo.isSuccess()) {
                return serviceStatusInfo;
            }
            long result = this.iProductMapper.updatePublishs(id, publish, (long) serviceStatusInfo.getData());
            redisTemplate.delete(MainKeyType.MAINPRODUCT);
            return new ServiceStatusInfo<>(0, "", result);
        } catch (Exception e) {
            return new ServiceStatusInfo<>(1, "上下架失败" + e.getMessage(), 0L);
        }
    }

    @Override
    public ServiceStatusInfo<ProductsOut> selectById(long id) {
        try {
            Products products = this.iProductMapper.selectById(id);
            ProductSKUs productSKUs = iProductSKUsMapper.selectByProductId(products.getId());
            products.setPromotionPrice(productSKUs.getPromotionPrice());
            products.setOriginalPrice(productSKUs.getOriginalPrice());
            ProductsOut productsOut = new ProductsOut();
            if (products.getProductDetailType() == ProductDetailType.CARD) {
                ProductCard productCard = this.iProductCardMapper.selectByProductId(products.getId());
                products.setStackUse(productCard.isStackUse());
                products.setAppointment(productCard.getAppointment());
                productsOut.setProductCard(productCard);
            }
            if (products.getProductDetailType() == ProductDetailType.CASHCOUPON) {
                ProductCashCoupon productCashCoupon = this.iProductCashCouponMapper.selectByProductId(products.getId());
                products.setStackUse(productCashCoupon.isStackUse());
                products.setAppointment(productCashCoupon.getAppointment());
                productsOut.setProductCashCoupon(productCashCoupon);
            }
            productsOut.setProducts(products);
            productsOut.setProductSKUs(productSKUs);
            return new ServiceStatusInfo<>(0, "", productsOut);
        } catch (Exception e) {
            return new ServiceStatusInfo<>(0, "查询单个商品失败" + e.getMessage(), null);
        }
    }

    @Override
    public ServiceStatusInfo<ProductOut> selectByIdPartial(long id) {
        try {
            return new ServiceStatusInfo<>(0, "", iProductMapper.selectByIdNoDelete(id));
        } catch (Exception e) {
            return new ServiceStatusInfo<>(1, "批量失败" + e.getMessage(), null);
        }
    }

    @Override
    public ServiceStatusInfo<Long> deleteByProducts(Long[] id) {
        try {
            ServiceStatusInfo serviceStatusInfo = judgeStoreId();
            if (!serviceStatusInfo.isSuccess()) {
                return serviceStatusInfo;
            }
            long result = this.iProductMapper.deleteByProducts(id, (long) serviceStatusInfo.getData());
            redisTemplate.delete(MainKeyType.MAINPRODUCT);
            return new ServiceStatusInfo<>(0, "", result);
        } catch (Exception e) {
            return new ServiceStatusInfo<>(1, "批量失败" + e.getMessage(), 0L);
        }
    }

    @Override
    public long getProductInventoryNum(long productId) {
        long allInventory = this.iProductMapper.getProductInventory(productId);
        return allInventory;
    }

    @Override
    public ServiceStatusInfo<List<Products>> searchCondition(SearchProducts searchProduct, int type, int pageNo, int rows) {
        List<Products> result = null;
        try {
            ServiceStatusInfo serviceStatusInfo = judgeStoreId();
            if (!serviceStatusInfo.isSuccess()) {
                return serviceStatusInfo;
            }
            searchProduct.setStoreId((long) serviceStatusInfo.getData());
            PageHelper.startPage(pageNo, rows);
            result = this.iProductMapper.searchCondition(searchProduct, type);
            for (Products pro : result) {
                ProductSKUs productSKUs = this.iProductSKUsMapper.selectByProductId(pro.getId());
                pro.setOriginalPrice(productSKUs.getOriginalPrice());
                pro.setPromotionPrice(productSKUs.getPromotionPrice());
            }
            return new ServiceStatusInfo<>(0, "", result);
        } catch (Exception e) {
            return new ServiceStatusInfo<>(1, "查询失败" + e.getMessage(), result);
        }
    }

    /**
     * 判断是不是店铺
     *
     * @return
     */
    public ServiceStatusInfo<?> judgeStoreId() {
        AuthUser authUser = tokenCenterManager.fetchUser(String.valueOf(JWTUtil.getCurrentId())).getData();
        if (authUser == null) {
            return new ServiceStatusInfo(1, "查询失败:用户不存在", null);
        }
        if (authUser.getLegalSubjectId() == 0) {
            return new ServiceStatusInfo(1, "用户不是商户", null);
        }
        long storeId = storeServiceImpl.selectByLegalSubjectId(authUser.getLegalSubjectId()).getData().getId();
        if (storeId <= 0) {
            return new ServiceStatusInfo(1, "用户没有店铺", null);
        }
        return new ServiceStatusInfo(0, "", storeId);
    }

    /**
     * 判断数据
     *
     * @param createProducts
     * @return
     */
    public ServiceStatusInfo judgeProducts(CreateProducts createProducts) {
        if (createProducts.getProductType() != 0 && createProducts.getProductType() != 1) {
            return new ServiceStatusInfo<>(1, "创建失败：产品类型不正确", null);
        }
        if (!(ProductDetailType.DELIVERY == createProducts.getProductDetailType()) && !(ProductDetailType.NODELIVERY == createProducts.getProductDetailType())
                && !(ProductDetailType.CARD == createProducts.getProductDetailType()) && !(ProductDetailType.CASHCOUPON == createProducts.getProductDetailType())) {
            return new ServiceStatusInfo<>(1, "创建失败：产品详细类型不正确", null);
        }
        if (ProductDetailType.CARD == createProducts.getProductDetailType() || ProductDetailType.CASHCOUPON == createProducts.getProductDetailType()) {
            if (!(ValidType.PAY_VALIDED == createProducts.getValidType()) && !(ValidType.PAY_VALIDED == createProducts.getValidType()) && !(ValidType.PAY_SPEC_HOUR_VALIDED == createProducts.getValidType())) {
                return new ServiceStatusInfo<>(1, "创建失败：validType类型不正确", null);
            }
            if (ValidType.PAY_VALIDED == createProducts.getValidType() && createProducts.getSpecHoursValid() <= 0 && createProducts.getValidDays() <= -2 && createProducts.getValidStartTime() == null && createProducts.getValidEndTime() == null) {
                return new ServiceStatusInfo<>(1, "创建失败：PAY_VALIDED生效类型不正确", null);
            }
            if (ValidType.PAY_NEXTDAY_VALIDED == createProducts.getValidType() && createProducts.getValidDays() <= -2 && createProducts.getValidStartTime() == null && createProducts.getValidEndTime() == null) {
                return new ServiceStatusInfo<>(1, "创建失败：PAY_NEXTDAY_VALIDED生效类型不正确", null);
            }
            if (ValidType.PAY_SPEC_HOUR_VALIDED == createProducts.getValidType() && createProducts.getValidStartTime() == null && createProducts.getValidEndTime() == null) {
                return new ServiceStatusInfo<>(1, "创建失败：PAY_SPEC_HOUR_VALIDED生效类型不正确", null);
            }
        }
        return null;
    }

    @Override
    public ServiceStatusInfo<List<StoreProduct>> selectProductByStoreId(long storeId) {
        List<StoreProduct> result = null;
        try {
            result = iProductMapper.selectProductByStoreId(storeId);
            return new ServiceStatusInfo<>(0, "", result);
        } catch (Exception e) {
            return new ServiceStatusInfo<>(1, "查询店铺商品失败" + e.getMessage(), null);
        }

    }
}
