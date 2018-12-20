package com.zwdbj.server.adminserver.service.setting.service;

import com.zwdbj.server.adminserver.service.setting.model.AppPushSettingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class SettingService {
    @Autowired
    private RedisTemplate redisTemplate;
    private static String appPushSettingCacheHashKey = "setting_push_hash_cache_key";

    public AppPushSettingModel get(long userId) {
        //判断redis中是否已存在当前用户的推送设置，存在则取出，不存在则设置为默认设置并存入redis
        boolean isExist = this.redisTemplate.opsForHash().hasKey(appPushSettingCacheHashKey, String.valueOf(userId));
        if (isExist) {
            AppPushSettingModel settingModel = (AppPushSettingModel) this.redisTemplate.opsForHash().get(appPushSettingCacheHashKey, String.valueOf(userId));
            return settingModel;
        }
        AppPushSettingModel defaultPushSetting = defaultPushSetting();
        this.redisTemplate.opsForHash().put(appPushSettingCacheHashKey, String.valueOf(userId), defaultPushSetting);
        return defaultPushSetting;
    }

    //默认推送设置
    protected AppPushSettingModel defaultPushSetting() {
        AppPushSettingModel appPushSettingModel = new AppPushSettingModel();
        appPushSettingModel.setCommentIsOpen(true);
        appPushSettingModel.setHeartIsOpen(true);
        appPushSettingModel.setMyFollowedLivingIsOpen(true);
        appPushSettingModel.setMyFollowedPubNewVideoIsOpen(true);
        appPushSettingModel.setSystemIsOpen(true);
        appPushSettingModel.setNewFollowerIsOpen(true);
        return appPushSettingModel;
    }

    public AppPushSettingModel set(AppPushSettingModel model, long userId) {
        AppPushSettingModel innerModel = model;
        if (innerModel == null) {
            innerModel = defaultPushSetting();
        }
        //将用户推送设置存入redis中
        this.redisTemplate.opsForHash().put(appPushSettingCacheHashKey, String.valueOf(userId), innerModel);
        return innerModel;
    }
}
