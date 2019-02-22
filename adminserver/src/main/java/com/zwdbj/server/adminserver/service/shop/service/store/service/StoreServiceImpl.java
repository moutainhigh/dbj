package com.zwdbj.server.adminserver.service.shop.service.store.service;

import com.zwdbj.server.adminserver.QueueUtil;
import com.zwdbj.server.adminserver.service.shop.service.discountCoupon.model.DiscountCouponModel;
import com.zwdbj.server.adminserver.service.shop.service.discountCoupon.service.DiscountCouponServiceImpl;
import com.zwdbj.server.adminserver.service.shop.service.legalSubject.service.ILegalSubjectService;
import com.zwdbj.server.adminserver.service.shop.service.offlineStoreExtraServices.model.OfflineStoreExtraServices;
import com.zwdbj.server.adminserver.service.shop.service.offlineStoreExtraServices.service.OfflineStoreExtraServicesServiceImpl;
import com.zwdbj.server.adminserver.service.shop.service.offlineStoreOpeningHour.model.OfflineStoreOpeningHours;
import com.zwdbj.server.adminserver.service.shop.service.offlineStoreOpeningHour.service.OfflineStoreOpeningHoursServiceImpl;
import com.zwdbj.server.adminserver.service.shop.service.offlineStoreServiceScopes.model.OfflineStoreServiceScopes;
import com.zwdbj.server.adminserver.service.shop.service.offlineStoreServiceScopes.service.OfflineStoreServiceScopesServiceImpl;
import com.zwdbj.server.adminserver.service.shop.service.shopdetail.model.DiscountCoupon;
import com.zwdbj.server.adminserver.service.shop.service.shopdetail.model.DiscountCouponDetail;
import com.zwdbj.server.adminserver.service.shop.service.store.mapper.IStoreMapper;
import com.zwdbj.server.adminserver.service.shop.service.store.model.*;
import com.zwdbj.server.adminserver.service.shop.service.storeReview.model.BusinessSellerReviewModel;
import com.zwdbj.server.adminserver.service.shop.service.storeReview.service.StoreReviewService;
import com.zwdbj.server.probuf.middleware.mq.QueueWorkInfoModel;
import com.zwdbj.server.adminserver.service.shop.service.store.model.StoreInfo;
import com.zwdbj.server.adminserver.service.shop.service.store.model.StoreSearchInput;
import com.zwdbj.server.adminserver.service.shop.service.store.model.StoreSimpleInfo;
import com.zwdbj.server.utility.model.ServiceStatusInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StoreServiceImpl implements StoreService {

    @Autowired
    private IStoreMapper iStoreMapper;
    @Autowired
    private OfflineStoreExtraServicesServiceImpl extraServicesService;
    @Autowired
    private OfflineStoreOpeningHoursServiceImpl openingHoursService;
    @Autowired
    private OfflineStoreServiceScopesServiceImpl serviceScopesService;
    @Autowired
    private DiscountCouponServiceImpl discountCouponService;
    @Autowired
    ILegalSubjectService legalSubjectServiceImpl;
    @Autowired
    StoreReviewService storeReviewServiceImpl;

    @Override
    public ServiceStatusInfo<StoreSimpleInfo> selectByLegalSubjectId(long legalSubjectId) {
        try {
            StoreSimpleInfo info = iStoreMapper.selectByLegalSubjectId(legalSubjectId);
            return new ServiceStatusInfo<>(0, "", info);
        } catch (Exception e) {
            return new ServiceStatusInfo<>(1, "查询失败" + e.getMessage(), null);
        }
    }

    @Override
    public ServiceStatusInfo<List<StoreSimpleInfo>> searchStore(StoreSearchInput input) {
        try {
            List<StoreSimpleInfo> storeSimpleInfos = this.iStoreMapper.searchStore(input);
            for (StoreSimpleInfo info:storeSimpleInfos){
                ServiceStatusInfo<List<String>> serviceScopes = serviceScopesService.selectCateNameByofflineStoreId(info.getId());
                if (serviceScopes.getData()!=null || serviceScopes.getData().size()!=0)
                info.setServiceScopes(serviceScopes.getData().toString());
            }
            return new ServiceStatusInfo<>(0, "", storeSimpleInfos);
        }catch (Exception e){
            return new ServiceStatusInfo<>(1, "查询失败" + e.getMessage(), null);
        }
    }

    @Override
    public ServiceStatusInfo<StoreInfo> selectByStoreId(long storeId) {
        StoreInfo dto = null;
        try {
            dto = iStoreMapper.selectByStoreId(storeId);
            if (dto==null)return new ServiceStatusInfo<>(1, "查询失败" , null);
            List<OfflineStoreExtraServices> extraServices = extraServicesService.selectByofflineStoreId(storeId).getData();
            List<OfflineStoreOpeningHours> openingHours = openingHoursService.select(storeId).getData();
            List<OfflineStoreServiceScopes> serviceScopes = serviceScopesService.selectByofflineStoreId(storeId).getData();
            List<DiscountCouponModel> disCountCoupon = discountCouponService.selectByStoreId(storeId).getData();
            long legalSubjectId = dto.getLegalSubjectId();
            List<BusinessSellerReviewModel> reviewModels=null;
            if (legalSubjectId!=0)
             reviewModels = this.storeReviewServiceImpl.getStoreReviewById(legalSubjectId).getData();
            if (openingHours!=null)
                dto.setOpeningHours(openingHours);
            if (disCountCoupon!=null)
                dto.setDiscountCoupons(disCountCoupon);
            if (extraServices!=null)
                dto.setExtraServices(extraServices);
            if (serviceScopes!=null)
                dto.setServiceScopes(serviceScopes);
            if (reviewModels!=null)
                dto.setBusinessSellerReviewModels(reviewModels);
            return new ServiceStatusInfo<>(0, "", dto);
        } catch (Exception e) {
            return new ServiceStatusInfo<>(1, "查询失败" + e.getMessage(), dto);
        }
    }

    public long selectTenantId(long legalSubjectId) {
        return iStoreMapper.selectTenantId(legalSubjectId);

    }

    @Override
    @Transactional
    public ServiceStatusInfo<Integer> updateStoreStatus(long storeId,long legalSubjectId, int state) {
        int result = this.iStoreMapper.updateStoreStatus(storeId,state);
        if (result==0)return new ServiceStatusInfo<>(1,"店铺更新失败了",result);
        int s = this.legalSubjectServiceImpl.updateStatusById(legalSubjectId,state);
        if (s==0)return new ServiceStatusInfo<>(1,"商家更新失败了",result);
        QueueUtil.sendQueue(storeId, QueueWorkInfoModel.QueueWorkModifyShopInfo.OperationEnum.UPDATE);
        return new ServiceStatusInfo<>(0,"",s);
    }

    @Override
    @Transactional
    public ServiceStatusInfo<Integer> reviewStore(long storeId, long legalSubjectId, ReviewStoreInput input) {
        //审核所有的需要审核的资料
        int a = this.storeReviewServiceImpl.reviewStore(legalSubjectId,input).getData();
        if (a==0)return new ServiceStatusInfo<>(1,"资料审核失败",0);
        //审核store
        a=this.iStoreMapper.reviewStore(storeId,input.isReviewOrNot());
        if (a==0)return new ServiceStatusInfo<>(1,"店铺审核失败",0);
        //审核 legalSubject
        a=this.legalSubjectServiceImpl.verityUnReviewedLegalSubject(legalSubjectId,input).getData();
        if (a==0)return new ServiceStatusInfo<>(1,"商家审核失败",0);
        QueueUtil.sendQueue(storeId, QueueWorkInfoModel.QueueWorkModifyShopInfo.OperationEnum.UPDATE);
        return  new ServiceStatusInfo<>(0,"审核成功",a);
    }
}