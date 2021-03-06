package com.zwdbj.server.adminserver.service.shop.service.shoppingCart.service;

import com.zwdbj.server.adminserver.service.shop.service.shoppingCart.model.ProductCartAddInput;
import com.zwdbj.server.adminserver.service.shop.service.shoppingCart.model.ProductCartModel;
import com.zwdbj.server.adminserver.service.shop.service.shoppingCart.model.ProductCartModifyInput;
import com.zwdbj.server.basemodel.model.ServiceStatusInfo;

import java.util.List;

public interface ShoppingCartService {
    List<ProductCartModel> findAllShoppingCarts();
    ServiceStatusInfo<ProductCartModel> getShoppingCartById(long id);
    ServiceStatusInfo<Integer> addShoppingCart(ProductCartAddInput input);
    ServiceStatusInfo<Integer> modifyShoppingCart(long id, ProductCartModifyInput input);
    ServiceStatusInfo<Integer> notRealDeleteShoppingCart(long id);
}
