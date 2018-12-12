package com.zwdbj.server.mobileapi.service.userAssets.service;

import com.zwdbj.server.mobileapi.service.userAssets.mapper.IUserAssetMapper;
import com.zwdbj.server.mobileapi.service.userAssets.model.*;
import com.zwdbj.server.utility.common.UniqueIDCreater;
import com.zwdbj.server.utility.common.shiro.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserAssetServiceImpl implements IUserAssetService{
    @Autowired
    IUserAssetMapper userAssetMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Transactional
    public UserAssetModel getCoinsByUserId(long userId) {
        boolean isExist =  this.userAssetIsExistOrNot(userId);
        if (!isExist){
            this.greatUserAsset(userId);
        }
        UserAssetModel userAssetModel= this.userAssetMapper.getCoinsByUserId(userId);
        //加入缓存
        String key = "USERASSET"+userAssetModel.getUserId();
        ValueOperations<String,UserAssetModel> operations = redisTemplate.opsForValue();
        operations.set(key,userAssetModel);
        return userAssetModel;
    }

    @Transactional
    public UserAssetModel getCoinsByUserId(){
        long userId = JWTUtil.getCurrentId();
        return getCoinsByUserId(userId);
    }
    @Transactional
    public int updateUserAsset(long coins){
        long userId = JWTUtil.getCurrentId();
        return updateUserAsset(userId,coins);
    }
    public int updateUserAsset(long userId,long coins) {
        int result = this.userAssetMapper.updateUserAsset(userId,coins);
        return result;
    }
    @Transactional
    public int greatUserAsset(){
        long userId = JWTUtil.getCurrentId();
        int result = this.greatUserAsset(userId);
        return result;
    }
    @Transactional
    public int greatUserAsset(long userId){
        long id = UniqueIDCreater.generateID();
        int result = this.userAssetMapper.greatUserAsset(id,userId);
        return result;
    }
    @Transactional
    public boolean userAssetIsExistOrNot(long userId){
        int result = this.userAssetMapper.userAssetIsExistOrNot(userId);
        return result!=0;
    }


    /**
     * coinType
     */

    @Transactional
    public UserCoinTypeModel getUserCoinType(String type){
        long userId = JWTUtil.getCurrentId();
        return getUserCoinType(userId,type);
    }

    @Transactional
    public UserCoinTypeModel getUserCoinType(long userId,String type) {
        boolean isExist = this.userCoinTypeIsExist(userId,type);
        if (!isExist){
            this.greatUserCoinType(userId,type);
        }
        UserCoinTypeModel userCoinTypeModel = this.userAssetMapper.getUserCoinType(userId,type);
        return userCoinTypeModel;
    }

    @Transactional
    public int  greatUserCoinType(long userId,String type){
        long id  = UniqueIDCreater.generateID();
        int result = this.userAssetMapper.greatUserCoinType(id,userId,type);
        return result;

    }

    @Transactional
    public boolean userCoinTypeIsExist(long userId,String type){
        int result = this.userAssetMapper.userCoinTypeIsExist(userId,type);
        return result!=0;
    }

    @Transactional
    public int updateUserCoinType(String type,int num){
        long userId = JWTUtil.getCurrentId();
        return this.updateUserCoinType(userId,type,num);
    }

    @Transactional
    public int updateUserCoinType(long userId,String type,int num){
        int result = this.userAssetMapper.updateUserCoinType(userId,type,num);
        return result;
    }

    //coinDetails

    @Transactional
    public List<UserCoinDetailsModel> getUserCoinDetails(long userId) {
        List<UserCoinDetailsModel> userCoinDetailsModels = this.userAssetMapper.getUserCoinDetails(userId);
        return userCoinDetailsModels;
    }

    @Transactional(readOnly = true)
    public List<UserCoinDetailsModel> getUserCoinDetails(){
        long userId = JWTUtil.getCurrentId();
        return getUserCoinDetails(userId);
    }

    @Override
    @Transactional
    public long addUserCoinDetail(UserCoinDetailAddInput input){
        long userId = JWTUtil.getCurrentId();
        return addUserCoinDetail(userId,input);
    }
    @Override
    @Transactional
    public long addUserCoinDetail(long userId,UserCoinDetailAddInput input) {
        long id = UniqueIDCreater.generateID();
        this.userAssetMapper.addUserCoinDetail(id,userId,input);
        return id;
    }

    @Override
    @Transactional
    public int updateUserCoinDetail(UserCoinDetailModifyInput input){
        UserAssetNumAndStatus  u = this.userAssetMapper.findUserCoinDetailById(input.getId());
        if ("PROCESSING".equals(u.getStatus())){
            int result = this.userAssetMapper.updateUserCoinDetail(input);
            if (result==1 && input.getStatus().equals("SUCCESS")){
                boolean a = this.userCoinTypeIsExist(u.getUserId(),"PAY");
                if (!a)this.greatUserCoinType(u.getUserId(),"PAY");
                result = this.updateUserCoinType(u.getUserId(),input.getType(),u.getNum());
                if (result==1){
                    boolean b = this.userAssetIsExistOrNot(u.getUserId());
                    if (!b)this.greatUserAsset(u.getUserId());
                    result = this.updateUserAsset(u.getUserId(),u.getNum());
                    return result;
                }else {
                    return 0;
                }
            }else {
                return 0;
            }
        }else {
            return 0;
        }

    }


    public  List<BuyCoinConfigModel> findAllBuyCoinConfigs(){
        List<BuyCoinConfigModel> buyCoinConfigModels = this.userAssetMapper.findAllBuyCoinConfigs();
        return buyCoinConfigModels;
    }

}
