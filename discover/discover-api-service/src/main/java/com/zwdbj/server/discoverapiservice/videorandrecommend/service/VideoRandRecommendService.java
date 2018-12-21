package com.zwdbj.server.discoverapiservice.videorandrecommend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 随机推荐视频策略
 */
@Service
public class VideoRandRecommendService {
    @Autowired
    private RedisTemplate redisTemplate;
    private String videosCacheKey = "randvideo.all";
    private String userLoadedVideoCacheKey(String id) {
        return "randvideo.all.user."+id;
    }
    /***
     * 缓存系统新的视频
     * @param id
     */
    public void pushNewVideo(Long id) {
        this.redisTemplate.opsForSet().add(videosCacheKey,id);
    }

    /**
     * 某个用户获取一定数量的视频
     * @param userId
     * @param count
     * @return 视频ID列表
     */
    public List<Long> fetchVideo(String userId, int count) {
        String userCacheKey = userLoadedVideoCacheKey(userId);
        String diffcacheKey = "randvideo.all.user.diff."+userId;
        int num = this.redisTemplate.opsForSet().differenceAndStore(videosCacheKey,userCacheKey,diffcacheKey).intValue();
        List<Long> lst = null;
        if (num==0) {
            this.redisTemplate.opsForSet().pop(userCacheKey,this.redisTemplate.opsForSet().size(userCacheKey));
            lst = this.redisTemplate.opsForSet().randomMembers(videosCacheKey,count);
        } else {
            int cn = count;
            if (count>num) {
                cn = num;
            }
            lst = this.redisTemplate.opsForSet().randomMembers(diffcacheKey,cn);
        }
        for (Long l: lst) {
            this.redisTemplate.opsForSet().add(userCacheKey,l);
        }
        return lst;
    }

    /**
     * 删除某个视频
     * @param id
     */
    public void popVideo(Long id) {
        this.redisTemplate.opsForSet().remove(videosCacheKey,id);
    }
}