package com.zwdbj.server.mobileapi.service.userAssets.service;

import com.zwdbj.server.mobileapi.service.userAssets.mapper.IUserAssetMapper;
import com.zwdbj.server.mobileapi.service.userAssets.model.*;
import com.zwdbj.server.utility.common.UniqueIDCreater;
import com.zwdbj.server.utility.common.shiro.JWTUtil;
import com.zwdbj.server.utility.model.ServiceStatusInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
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
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Transactional
    public  ServiceStatusInfo<Long> getCoinsByUserId(long userId) {
        boolean isExist =  this.userAssetIsExistOrNot(userId);
        if (!isExist){
            this.greatUserAsset(userId);
        }
        /*String key = "USERASSET_"+userId;
        Long coins;
        //加入缓存
        if (!this.stringRedisTemplate.hasKey(key)){
            coins= this.userAssetMapper.getCoinsByUserId(userId);
            this.stringRedisTemplate.opsForValue().set(key,String.valueOf(coins));
        }else {
            coins = Long.valueOf(this.stringRedisTemplate.opsForValue().get(key));
        }*/
        Long coins= this.userAssetMapper.getCoinsByUserId(userId);

        return new ServiceStatusInfo<>(0,"",coins);
    }

    @Transactional
    public ServiceStatusInfo<Long> getCoinsByUserId(){
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
        this.getCoinsByUserId(userId);
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
    public ServiceStatusInfo<UserCoinTypeModel> getUserCoinType(String type){
        long userId = JWTUtil.getCurrentId();
        return getUserCoinType(userId,type);
    }

    @Transactional
    public ServiceStatusInfo<UserCoinTypeModel> getUserCoinType(long userId,String type) {
        boolean isExist = this.userCoinTypeIsExist(userId,type);
        if (!isExist){
            this.greatUserCoinType(userId,type);
        }
        /*Long coins ;
        String key = "USERASSET_"+type+"_"+userId;
        if (!this.stringRedisTemplate.hasKey(key)){
            coins= this.userAssetMapper.getUserCoinType(userId,type);
            this.stringRedisTemplate.opsForValue().set(key,String.valueOf(coins));
        }else {
            coins = Long.valueOf(this.stringRedisTemplate.opsForValue().get(key));
        }*/
        UserCoinTypeModel model= this.userAssetMapper.getUserCoinType(userId,type);
        if (model!=null)model.setMoney(model.getCoins()*10);
        return new ServiceStatusInfo<>(0,"",model);
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

    /**
     * 内部
     * @param userId
     * @param input
     * @return
     */
    @Transactional
    public int addUserCoinDetailSuccess(long userId,UserCoinDetailAddInput input) {
        long id = UniqueIDCreater.generateID();
        int result = this.userAssetMapper.addUserCoinDetailSuccess(id,userId,input);
        return result;
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
    public void userIsExist(long userId){
        boolean a = this.userCoinTypeIsExist(userId,"TASK");
        if (!a)this.greatUserCoinType(userId,"TASK");
        boolean b = this.userCoinTypeIsExist(userId,"PAY");
        if (!b)this.greatUserCoinType(userId,"PAY");
        boolean c = this.userCoinTypeIsExist(userId,"OTHER");
        if (!c)this.greatUserCoinType(userId,"OTHER");
        boolean d = this.userCoinTypeIsExist(userId,"INCOME");
        if (!d)this.greatUserCoinType(userId,"INCOME");
        boolean e = this.userAssetIsExistOrNot(userId);
        if (!e)this.greatUserAsset(userId);

    }


    //视频的打赏详情
    /**
     * 视频的打赏详情
     */
    public ServiceStatusInfo<List<VideoTipDetails>> getVideoTipDetails(Long videoId) {
        List<VideoTipDetails> result = null;
        try {
            result = this.userAssetMapper.findVideoTipDetails(videoId);
            return new ServiceStatusInfo<>(0, "", result);
        } catch (Exception e) {
            return new ServiceStatusInfo<>(1, "查询视频打赏详情失败" + e.getMessage(), null);
        }
    }

    public int addVideoTipDetail(long videoId,long userId,int tipCoins){
            long id = UniqueIDCreater.generateID();
            int result = this.userAssetMapper.addVideoTipDetail(id,videoId,userId,tipCoins);
            return result;
    }



}
