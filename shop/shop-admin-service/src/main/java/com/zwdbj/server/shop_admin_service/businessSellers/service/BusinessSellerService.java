package com.zwdbj.server.shop_admin_service.businessSellers.service;

import com.zwdbj.server.shop_admin_service.businessSellers.model.BusinessSellerAddInput;
import com.zwdbj.server.shop_admin_service.businessSellers.model.BusinessSellerDto;
import com.zwdbj.server.shop_admin_service.businessSellers.model.BusinessSellerModel;
import com.zwdbj.server.shop_admin_service.businessSellers.model.BusinessSellerModifyInput;
import com.zwdbj.server.utility.model.ServiceStatusInfo;

import java.util.List;

public interface BusinessSellerService {
    List<BusinessSellerDto> findAllBusinessSellers();

    ServiceStatusInfo<Integer> modifyBusinessSellers(long id, BusinessSellerModifyInput input);

    ServiceStatusInfo<Integer> addBusinessSellers(BusinessSellerAddInput input);

    ServiceStatusInfo<Integer> deleteBusinessSellers(long id);


    ServiceStatusInfo<BusinessSellerModel> getBusinessSellerById(long businessSellerId);

    ServiceStatusInfo<Integer> closeBusinessSellers(long id);

}
