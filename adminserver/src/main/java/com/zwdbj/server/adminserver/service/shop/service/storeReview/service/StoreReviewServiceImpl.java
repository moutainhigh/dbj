package com.zwdbj.server.adminserver.service.shop.service.storeReview.service;

import com.zwdbj.server.adminserver.middleware.mq.ESUtil;
import com.zwdbj.server.adminserver.service.shop.service.store.model.ReviewStoreInput;
import com.zwdbj.server.adminserver.service.shop.service.store.service.StoreServiceImpl;
import com.zwdbj.server.adminserver.service.shop.service.storeReview.mapper.IStoreReviewMapper;
import com.zwdbj.server.adminserver.service.shop.service.storeReview.model.BusinessSellerReviewModel;
import com.zwdbj.server.adminserver.service.shop.service.storeReview.model.StoreReviewAddInput;
import com.zwdbj.server.basemodel.model.ServiceStatusInfo;
import com.zwdbj.server.common.qiniu.QiniuService;
import com.zwdbj.server.utility.common.UniqueIDCreater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StoreReviewServiceImpl implements StoreReviewService {
    @Autowired
    IStoreReviewMapper storeReviewMapper;
    @Autowired
    private ESUtil esUtil;
    @Autowired
    QiniuService qiniuService;
    @Autowired
    private StoreServiceImpl storeServiceImpl;

    @Override
    public List<BusinessSellerReviewModel> findAllStoreReviews() {
        List<BusinessSellerReviewModel> businessSellerReviewModels = this.storeReviewMapper.findAllStoreReviews();
        return businessSellerReviewModels;
    }

    @Override
    public ServiceStatusInfo<Integer> modifyStoreReview(long id, StoreReviewAddInput input) {
        try {
            int result = this.storeReviewMapper.modifyStoreReview(id, input);
            if (result == 0) return new ServiceStatusInfo<>(1, "修改认证信息失败", result);
            long storeId = storeServiceImpl.selectStoreIdByLegalSubjectId(input.getLegalSubjectId());
            esUtil.QueueWorkInfoModelSend(storeId, "shop", "u");

            return new ServiceStatusInfo<>(0, "修改认证信息成功", result);
        } catch (Exception e) {
            return new ServiceStatusInfo<>(1, "修改认证信息失败" + e.getMessage(), null);
        }
    }

    @Override
    public ServiceStatusInfo<Integer> addStoreReview(StoreReviewAddInput input) {
        try {
            String reviewData = input.getReviewData();
            StringBuilder reviewDatas = new StringBuilder();
            if (reviewData != null && reviewData.length() > 0) {
                String[] reviews = reviewData.split(",");
                for (String s : reviews) {
                    String ss = this.qiniuService.url(s) + ",";
                    reviewDatas.append(ss);
                }
            }
            reviewData = reviewDatas.toString();
            reviewData = reviewData.substring(0, (reviewData.length() - 1));
            input.setReviewData(reviewData);
            long id = UniqueIDCreater.generateID();
            int result = this.storeReviewMapper.addStoreReview(id, input);
            if (result == 0) return new ServiceStatusInfo<>(1, "添加认证信息失败", result);
            long storeId = storeServiceImpl.selectStoreIdByLegalSubjectId(input.getLegalSubjectId());
            esUtil.QueueWorkInfoModelSend(storeId, "shop", "u");
            return new ServiceStatusInfo<>(0, "添加认证信息成功", result);
        } catch (Exception e) {
            return new ServiceStatusInfo<>(1, "添加认证信息失败" + e.getMessage(), null);
        }
    }

    @Override
    public ServiceStatusInfo<Integer> deleteStoreReview(long id, long legalSubjectId) {
        try {
            int result = this.storeReviewMapper.deleteStoreReview(id);
            if (result == 0) return new ServiceStatusInfo<>(1, "删除认证信息失败", result);
            long storeId = storeServiceImpl.selectStoreIdByLegalSubjectId(legalSubjectId);
            esUtil.QueueWorkInfoModelSend(storeId, "shop", "u");

            return new ServiceStatusInfo<>(0, "删除认证信息成功", result);
        } catch (Exception e) {
            return new ServiceStatusInfo<>(1, "删除认证信息失败" + e.getMessage(), null);
        }
    }

    @Override
    public ServiceStatusInfo<Integer> reviewStore(long legalSubjectId, ReviewStoreInput input) {
        try {
            /*List<BusinessSellerReviewModel> businessSellerReviewModels = this.getStoreReviewById(legalSubjectId).getData();
            if (businessSellerReviewModels==null)return  new ServiceStatusInfo<>(0,"商铺资料审核成功",0);*/
            int status = 1;
            if (input.isReviewOrNot()) {
                status = 0;
            } else {
                status = 2;
            }
            int a = this.storeReviewMapper.reviewStore(legalSubjectId, input, status);
            long storeId = storeServiceImpl.selectStoreIdByLegalSubjectId(legalSubjectId);
            esUtil.QueueWorkInfoModelSend(storeId, "shop", "u");
            return new ServiceStatusInfo<>(0, "商铺资料审核成功", a);
        } catch (Exception e) {
            return new ServiceStatusInfo<>(1, "商铺资料审核失败" + e.getMessage(), null);
        }
    }

    @Override
    public ServiceStatusInfo<List<BusinessSellerReviewModel>> getStoreReviewById(long legalSubjectId) {
        try {
            List<BusinessSellerReviewModel> businessSellerReviewModels = this.storeReviewMapper.getStoreReviewById(legalSubjectId);
            return new ServiceStatusInfo<>(0, "获取商铺成功", businessSellerReviewModels);
        } catch (Exception e) {
            return new ServiceStatusInfo<>(1, "获取商铺失败" + e.getMessage(), null);
        }
    }

    @Override
    public ServiceStatusInfo<Integer> notRealDeleteStoreReview(long id, long legalSubjectId) {
        try {
            int result = this.storeReviewMapper.notRealDeleteStoreReview(id);
            if (result == 0) return new ServiceStatusInfo<>(1, "删除认证信息失败", result);
            long storeId = storeServiceImpl.selectStoreIdByLegalSubjectId(legalSubjectId);
            esUtil.QueueWorkInfoModelSend(storeId, "shop", "u");

            return new ServiceStatusInfo<>(0, "删除认证信息成功", result);
        } catch (Exception e) {
            return new ServiceStatusInfo<>(1, "删除认证信息失败" + e.getMessage(), null);
        }
    }
}
