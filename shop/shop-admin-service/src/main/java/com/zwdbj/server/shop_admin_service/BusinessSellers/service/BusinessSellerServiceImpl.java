package com.zwdbj.server.shop_admin_service.BusinessSellers.service;

import com.zwdbj.server.shop_admin_service.BusinessSellers.mapper.IBusinessSellerMapper;
import com.zwdbj.server.shop_admin_service.BusinessSellers.model.BusinessSellerAddInput;
import com.zwdbj.server.shop_admin_service.BusinessSellers.model.BusinessSellerModel;
import com.zwdbj.server.shop_admin_service.BusinessSellers.model.BusinessSellerModifyInput;
import com.zwdbj.server.utility.common.UniqueIDCreater;
import com.zwdbj.server.utility.model.ServiceStatusInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BusinessSellerServiceImpl implements BusinessSellerService{
    @Autowired
    IBusinessSellerMapper businessSellerMapper;
    @Override
    public List<BusinessSellerModel> findAllBusinessSellers() {
        List<BusinessSellerModel> businessSellerModels = this.businessSellerMapper.findAllBusinessSellers();
        return businessSellerModels;
    }

    @Override
    public ServiceStatusInfo<BusinessSellerModel> getBusinessSellerById(long businessSellerId) {
        try {
            BusinessSellerModel businessSeller = this.businessSellerMapper.getBusinessSellerById(businessSellerId);
            return new ServiceStatusInfo<>(0,"获取商铺成功",businessSeller);
        }catch (Exception e){
            return  new ServiceStatusInfo<>(1,"获取商铺失败"+e.getMessage(),null);
        }
    }

    @Override
    public ServiceStatusInfo<Integer> modifyBusinessSellers(BusinessSellerModifyInput input) {
        try {
            int result = this.businessSellerMapper.modifyBusinessSellers(input);
            if (result==0)return new ServiceStatusInfo<>(1,"修改商铺失败",result);
            return new ServiceStatusInfo<>(0,"修改商铺成功",result);
        }catch (Exception e){
            return  new ServiceStatusInfo<>(1,"修改商铺失败"+e.getMessage(),null);
        }

    }

    @Override
    public ServiceStatusInfo<Integer> addBusinessSellers(BusinessSellerAddInput input) {
        try {
            long id = UniqueIDCreater.generateID();
            int result = this.businessSellerMapper.addBusinessSellers(id,input);
            if (result==0)return new ServiceStatusInfo<>(1,"添加商铺失败",result);
            return new ServiceStatusInfo<>(0,"添加商铺成功",result);
        }catch (Exception e){
            return  new ServiceStatusInfo<>(1,"添加商铺失败"+e.getMessage(),null);
        }
    }

    @Override
    public ServiceStatusInfo<Integer> deleteBusinessSellers(long businessSellerId) {
        try {
            int result = this.businessSellerMapper.deleteBusinessSellers(businessSellerId);
            if (result==0)return new ServiceStatusInfo<>(1,"删除商铺失败",result);
            return new ServiceStatusInfo<>(0,"删除商铺成功",result);
        }catch (Exception e){
            return  new ServiceStatusInfo<>(1,"删除商铺失败"+e.getMessage(),null);
        }
    }
}