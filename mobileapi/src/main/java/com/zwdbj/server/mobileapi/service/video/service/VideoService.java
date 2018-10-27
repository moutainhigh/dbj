package com.zwdbj.server.mobileapi.service.video.service;

import com.github.pagehelper.Page;
import com.zwdbj.server.mobileapi.middleware.mq.QueueWorkInfoModel;
import com.zwdbj.server.mobileapi.model.EntityKeyModel;
import com.zwdbj.server.mobileapi.config.AppConfigConstant;
import com.zwdbj.server.mobileapi.service.ServiceStatusInfo;
import com.zwdbj.server.mobileapi.service.heart.service.HeartService;
import com.zwdbj.server.mobileapi.service.messageCenter.model.MessageInput;
import com.zwdbj.server.mobileapi.service.messageCenter.service.MessageCenterService;
import com.zwdbj.server.mobileapi.service.qiniu.service.QiniuService;
import com.zwdbj.server.mobileapi.service.resourceRefGoods.service.ResRefGoodsService;
import com.zwdbj.server.mobileapi.service.review.service.ReviewService;
import com.zwdbj.server.mobileapi.service.share.model.ShareDto;
import com.zwdbj.server.mobileapi.service.tag.service.TagService;
import com.zwdbj.server.mobileapi.service.user.model.UserModel;
import com.zwdbj.server.mobileapi.service.user.service.UserService;
import com.zwdbj.server.mobileapi.service.video.mapper.IVideoMapper;
import com.zwdbj.server.mobileapi.service.heart.model.HeartModel;
import com.zwdbj.server.mobileapi.model.HeartInput;
import com.zwdbj.server.mobileapi.service.video.model.*;
import com.zwdbj.server.mobileapi.shiro.JWTUtil;
import com.zwdbj.server.mobileapi.utility.UniqueIDCreater;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VideoService {
    @Autowired
    protected IVideoMapper videoMapper;
    @Autowired
    protected UserService userService;
    @Autowired
    protected QiniuService qiniuService;
    @Autowired
    protected HeartService heartService;
    @Autowired
    protected ResRefGoodsService resRefGoodsService;
    @Autowired
    protected TagService tagService;
    @Autowired
    protected MessageCenterService messageCenterService;
    @Autowired
    protected ReviewService reviewService;
    protected Logger logger = LoggerFactory.getLogger(VideoService.class);

    public ServiceStatusInfo<EntityKeyModel<String>> getGoods(long videoId) {
        String goods = this.resRefGoodsService.getGoods(videoId);
        if (goods==null) return new ServiceStatusInfo<>(1,"未找到关联的商品",null);
        return new ServiceStatusInfo<>(0,"",new EntityKeyModel<>(goods));
    }

    public long publicVideo(VideoPublishInput input) {

        String videoKey = input.getVideoKey();
        String coverImageKey = input.getCoverImageKey();
        String firstFrameImageKey = input.getFirstFrameUrl();

        input.setCoverImageKey(qiniuService.url(coverImageKey));
        input.setVideoKey(qiniuService.url(videoKey));
        input.setFirstFrameUrl(qiniuService.url(firstFrameImageKey));

        long videoId = UniqueIDCreater.generateID();
        long userId = JWTUtil.getCurrentId();
        if (userId<=0) return 0;
        if (input.getTags()!=null) {
            String[] tags = input.getTags().split(",");
            //TODO 更新标签的数据
        }
        videoMapper.publicVideo(videoId,userId,input);
        // 审核信息加入到消息队列
        if (videoKey!=null && videoKey.length() > 0) {
            QueueWorkInfoModel.QueueWorkQiniuWaitReviewResData resData
                    = QueueWorkInfoModel.QueueWorkQiniuWaitReviewResData.newBuilder()
                    .setDataId(videoId)
                    .setDataType(2)
                    .setResContent(videoKey)
                    .setResType(1)
                    .build();
            this.reviewService.reviewQiniuRes(resData);
        }
        if (coverImageKey!=null && coverImageKey.length()>0) {
            QueueWorkInfoModel.QueueWorkQiniuWaitReviewResData resData
                    = QueueWorkInfoModel.QueueWorkQiniuWaitReviewResData.newBuilder()
                    .setDataId(videoId)
                    .setDataType(2)
                    .setResContent(coverImageKey)
                    .setResType(0)
                    .build();
            this.reviewService.reviewQiniuRes(resData);
        }

        if ( firstFrameImageKey!=null && firstFrameImageKey.length()>0 && !(firstFrameImageKey.equals(coverImageKey))) {
            QueueWorkInfoModel.QueueWorkQiniuWaitReviewResData resData
                    = QueueWorkInfoModel.QueueWorkQiniuWaitReviewResData.newBuilder()
                    .setDataId(videoId)
                    .setDataType(2)
                    .setResContent(firstFrameImageKey)
                    .setResType(0)
                    .build();
            logger.info("我是没重复的第一帧"+firstFrameImageKey);
            this.reviewService.reviewQiniuRes(resData);
        }
        return videoId;
    }

    // TODO 未来优化性能检测
    public List<VideoInfoDto> nearby(double longitude,double latitude,float distance) {
        List<VideoInfoDto> videoInfoDtos = this.videoMapper.nearby(longitude, latitude, distance);
        if (videoInfoDtos==null) return null;
        for (VideoInfoDto dto:videoInfoDtos) {
            loadVideoInfoDto(dto);
        }
        return videoInfoDtos;
    }

    public List<VideoInfoDto> listHot(Page<VideoInfoDto> pageInfo) {
        List<VideoInfoDto> videoInfoDtos = this.videoMapper.listHot();
        if (videoInfoDtos==null) return null;
        for (VideoInfoDto dto:videoInfoDtos) {
            loadVideoInfoDto(dto);
        }
        return videoInfoDtos;
    }

    public List<VideoInfoDto> listLatest(Page<VideoInfoDto> pageInfo) {
        List<VideoInfoDto> videoInfoDtos = this.videoMapper.listLatest();
        if (videoInfoDtos==null) return null;
        for (VideoInfoDto dto:videoInfoDtos) {
            loadVideoInfoDto(dto);
        }
        return videoInfoDtos;
    }

    /**
     * 根据标签搜索短视频
     */
    public List<VideoInfoDto> listByTag(String tag){
        List<VideoInfoDto> videoInfoDtos = this.videoMapper.listByTag(tag);
        if (videoInfoDtos==null)return null;
        for (VideoInfoDto dto:videoInfoDtos) {
            loadVideoInfoDto(dto);
        }
        return videoInfoDtos;

    }

    /**
     * 获取userId关注的用户的视频列表
     * @param userId
     * @return
     */
    public List<VideoInfoDto> listByUserFollowed(long userId) {
        List<VideoInfoDto> dtos = this.videoMapper.myFollowedVideos(userId);
        if (dtos==null) return null;
        for(VideoInfoDto dto:dtos) {
            loadVideoInfoDto(dto);
        }
        return dtos;
    }

    public List<VideoInfoDto> videosByUser(long userId) {
        List<VideoInfoDto> dtos=null;
        if (userId==JWTUtil.getCurrentId()){
            dtos= this.videoMapper.videosByUser(userId);
        }if (userId!=JWTUtil.getCurrentId()){
            dtos= this.videoMapper.videosByUser1(userId);
        }

        if (dtos==null) return null;
        for(VideoInfoDto dto:dtos) {
            loadVideoInfoDto(dto);
        }
        return dtos;
    }

    public List<VideoInfoDto> videosByHearted(long userId) {
        List<VideoInfoDto> dtos = this.videoMapper.videosByHearted(userId);
        if (dtos==null) return null;
        for(VideoInfoDto dto:dtos) {
            loadVideoInfoDto(dto);
        }
        return dtos;
    }

    public VideoDetailInfoDto video(long id) {
        VideoDetailInfoDto dto = this.videoMapper.video(id);
        loadVideoInfoDto(dto);
        if (dto!=null) {
            dto.setLinkProductUrl(AppConfigConstant.getShopListUrl(id,"video"));
            dto.setShareTitle(dto.getTitle());
            dto.setShareContent(dto.getTitle());
            dto.setShareUrl(AppConfigConstant.getShareUrl(id,"video"));
        }
        return dto;
    }

    public void updatePlayCount(long id) {
        this.videoMapper.updateVideoField("playCount=playCount+1",id);
    }

    public void  updateShareCount(long id) {
        this.videoMapper.updateVideoField("shareCount=shareCount+1",id);
    }
    public void updateField(String fields,long id) {
        this.videoMapper.updateVideoField(fields,id);
    }
    public List<VideoInfoDto> next(long id) {
        List<VideoInfoDto> videoInfoDtos = this.videoMapper.next(id);
        for (VideoInfoDto dto:videoInfoDtos) {
            loadVideoInfoDto(dto);
        }
        return videoInfoDtos;
    }
    @Transactional
    public ServiceStatusInfo<Object> heart(HeartInput input) {
        long userId = JWTUtil.getCurrentId();
        if (userId<=0) return new ServiceStatusInfo<>(1,"请重新登录",null);
        HeartModel heartModel = this.heartService.findHeart(userId,input.getId());
        if (heartModel !=null && input.isHeart()) {
            return new ServiceStatusInfo<>(1,"已经点赞过",null);
        }
        if (heartModel ==null && !input.isHeart())
        {
            return new ServiceStatusInfo<>(0,"取消成功",null);
        }
        Long VUserId = this.videoMapper.findUserIdByVideoId(input.getId());
        if (input.isHeart()) {
            long id = UniqueIDCreater.generateID();
            this.heartService.heart(id,userId,input.getId(),0);
            this.videoMapper.addHeart(input.getId(),1);
            this.userService.addHeart(VUserId,1);
            VideoDetailInfoDto detailInfoDto = this.video(input.getId());
            if (detailInfoDto != null) {
                MessageInput msgInput = new MessageInput();
                msgInput.setCreatorUserId(userId);
                msgInput.setDataContent("{\"resId\":\""+input.getId()+"\",\"type\":\"0\"}");
                msgInput.setMessageType(1);
                this.messageCenterService.push(msgInput,detailInfoDto.getUserId());
            }
            return new ServiceStatusInfo<>(0,"点赞成功",null);
        } else {
            this.heartService.unHeart(userId,input.getId());
            this.videoMapper.addHeart(input.getId(),-1);
            this.userService.addHeart(VUserId,-1);
            return new ServiceStatusInfo<>(0,"取消成功",null);

        }
    }

    /**
     * TODO 调用此方法的源待优化
     * @param dto
     */
    protected void loadVideoInfoDto(VideoInfoDto dto) {
        if(dto==null)return;
        // TODO 考虑增加缓存||其他方式
        // 关注
        long uid = JWTUtil.getCurrentId();
        if (uid<=0) dto.setHearted(false);
        HeartModel heartModel = this.heartService.findHeart(uid,dto.getId());
        dto.setHearted(heartModel!=null);
        dto.setFollow(this.userService.isFollower(dto.getUserId(),uid));
        //头像
        UserModel userModel = this.userService.findUserById(dto.getUserId());
        if (userModel!=null) {
            dto.setUserAvatarUrl(userModel.getAvatarUrl());
            dto.setUserNickName(userModel.getNickName());
        }
    }

    public String findLinkPets(Long id){
        String linkPet = this.videoMapper.findLinkPets(id);
        if (linkPet==null)return null;
        return linkPet;
    }
    public ShareDto doShare(Long id){
        ShareDto sharedto = this.videoMapper.doShare(id);
        if (sharedto ==null) return null;
        if (sharedto.getTags()==null) sharedto.setTags("");
        return sharedto;
    }
    public void addShareCount(Long id){
        this.videoMapper.addShareCount(id);
    }


}
