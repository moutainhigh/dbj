package com.zwdbj.server.quartz.quartzService;

import com.zwdbj.server.operate.oprateService.OperateService;
import com.zwdbj.server.service.dailyIncreaseAnalysises.service.DailyIncreaseAnalysisesService;
import com.zwdbj.server.service.user.service.UserService;
import com.zwdbj.server.service.video.model.VideoHeartAndPlayCountDto;
import com.zwdbj.server.service.video.service.VideoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Service
public class QuartzService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    UserService userService;
    @Autowired
    VideoService videoService;
    @Autowired
    OperateService operateService;
    @Autowired
    DailyIncreaseAnalysisesService dailyIncreaseAnalysisesService;

    private Logger logger = LoggerFactory.getLogger(QuartzService.class);



    /**
     * 定时每天早上5点插入增量表的时间和id
     */

    public void everydayInsertTime(){
        int result =  this.dailyIncreaseAnalysisesService.isExistToday();
        if (result==0){
            this.dailyIncreaseAnalysisesService.everydayInsertTime();
            logger.info("我是加载时间");
        }else {
            logger.info("今日时间已加载...");
        }
    }

    /**
     * 定时每天凌晨3点插入昨天user和video的增量
     */

    public void everyIncreasedUsersAndVideos(){
        Long increasedVideos = this.videoService.everyIncreasedVideos();
        Long increasedUsers = this.userService.everyIncreasedUsers();
        this.dailyIncreaseAnalysisesService.everyIncreasedUsersAndVideos(increasedUsers,increasedVideos);
        logger.info("我是加载增长量");
    }


    /**
     * 每天增加马甲用户
     */
    public void greatVestUser(){
        try {
            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            int newUserNum = 0;
            if (this.stringRedisTemplate.hasKey(date)){
                newUserNum = Integer.valueOf(this.stringRedisTemplate.opsForValue().get(date));
            }else {
                this.operateService.userNamber();
            }
            if (newUserNum==0)return;
            for (int i = 1; i <= newUserNum; i++) {
                if (i%15==0){
                    this.operateService.newVestUser2(2);
                    logger.info("我是不同的马甲2: "+new SimpleDateFormat("HH:mm:ss").format(new Date()));
                }else if (i%15==1){
                    this.operateService.newVestUser2(3);
                    logger.info("我是不同的马甲3: "+new SimpleDateFormat("HH:mm:ss").format(new Date()));
                }else if (i%15==2){
                    this.operateService.newVestUser2(4);
                    logger.info("我是不同的马甲4: "+new SimpleDateFormat("HH:mm:ss").format(new Date()));
                }else if (i%15==3){
                    this.operateService.newVestUser2(5);
                    logger.info("我是不同的马甲5: "+new SimpleDateFormat("HH:mm:ss").format(new Date()));
                }else {
                    this.operateService.newVestUser1();
                    logger.info("我是默认马甲"+new SimpleDateFormat("HH:mm:ss").format(new Date()));
                }
            }
        }catch (Exception e){
            logger.info("增加用户异常"+e.getMessage());
        }
    }

    /**
     * 定时增加视频的播放量和点赞量
     */
    public void increaseHeartAndPlayCount() {
        try {
            logger.info("定时增加视频的播放量和点赞量++++++" + new SimpleDateFormat("HH:mm:ss").format(new Date()));
            List<VideoHeartAndPlayCountDto> videoHeartAndPlayCountDtos = this.videoService.findHeartAndPlayCount();
            if (videoHeartAndPlayCountDtos == null) return;
            int count =  new Double(Math.ceil(videoHeartAndPlayCountDtos.size()*0.8)).intValue();
            for (int j=0; j<count; j++) {
                VideoHeartAndPlayCountDto dto = videoHeartAndPlayCountDtos.get(this.operateService.getRandom(0,videoHeartAndPlayCountDtos.size()));
                int dianzhan = this.operateService.getRandom(20, 37);
                int pinlun = this.operateService.getRandom(4, 7);
                int fenxiang = this.operateService.getRandom(1, 3);
                int addPlayCount = this.operateService.getRandom(50, 201);
                this.videoService.updateField("playCount=playCount+" + addPlayCount, dto.getId());
                this.videoService.updateField("heartCount=heartCount+" + new Double(Math.ceil(addPlayCount * dianzhan / 100.0)).longValue(), dto.getId());
                Long addHeartCount = this.videoService.findVideoHeartCount(dto.getId()) - dto.getHeartCount();
                this.userService.updateField("totalHearts=totalHearts+" + addHeartCount, dto.getUserId());
                this.videoService.updateField("shareCount=shareCount+" + new Double(Math.ceil(addHeartCount * fenxiang / 100.0)).longValue(), dto.getId());
                int comment = (int) Math.ceil(addHeartCount * pinlun / 100.0);
                String redisComment =  this.stringRedisTemplate.opsForValue().get("REDIS_COMMENTS");
                String[] redisComments = redisComment.split(">");
                int size = redisComments.length;
                if (dto.getCommentCount()>=size)comment=0;
                int tem = 0;
                for (int i = 0; i < comment; i++) {
                    int gg = this.operateService.commentVideo1(dto.getId());
                    if (gg==0)tem--;
                }
                comment = comment + tem;
                if (comment==0)return;
                this.videoService.updateField("commentCount=commentCount+" + comment, dto.getId());
                logger.info("播放量不超过8000=++++++" + new SimpleDateFormat("HH:mm:ss").format(new Date()));
            }
        }catch(Exception e){
            logger.info("increaseHeartAndPlayCount异常" + e.getMessage());
        }

    }
    }