package com.zwdbj.server.mobileapi.service.shop.nearbyShops.service;

import com.zwdbj.server.mobileapi.service.shop.nearbyShops.model.DiscountCouponDetail;
import com.zwdbj.server.mobileapi.service.shop.nearbyShops.model.NearbyShop;
import com.zwdbj.server.mobileapi.service.shop.nearbyShops.model.ShopInfo;
import com.zwdbj.server.mobileapi.service.shop.nearbyShops.model.SuperStar;
import com.zwdbj.server.utility.model.ServiceStatusInfo;

import java.util.List;

public interface NearbyShopService {
    ServiceStatusInfo<ShopInfo> shopHomePage(long storeId);

    ServiceStatusInfo<SuperStar> superStar(long storeId);

    ServiceStatusInfo<DiscountCouponDetail> searchDiscountCouponDetail(long discountCouponId);

    ServiceStatusInfo<List<NearbyShop>> nearbyShopList(int pageNo);

    ServiceStatusInfo<List<NearbyShop>> searchShop(String search, String rank, double lat, double lon);
}
