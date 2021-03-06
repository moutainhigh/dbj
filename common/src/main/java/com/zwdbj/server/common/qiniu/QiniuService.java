package com.zwdbj.server.common.qiniu;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.pili.Client;
import com.qiniu.pili.Hub;
import com.qiniu.pili.PiliException;
import com.qiniu.pili.Stream;
import com.qiniu.processing.OperationManager;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.qiniu.util.UrlSafeBase64;
import com.zwdbj.server.config.settings.AppSettingConfigs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@Lazy
public class QiniuService {
    @Autowired
    private AppSettingConfigs appSettingConfigs;

    Logger logger = LoggerFactory.getLogger(QiniuService.class);


    public String uploadToken() {
        long expireTime = this.appSettingConfigs.getQiniuConfigs().getUptokenExpiretime();
        Auth auth = Auth.create(this.appSettingConfigs.getQiniuConfigs().getAccessKey(),
                this.appSettingConfigs.getQiniuConfigs().getAccessSecrect());
        String upToken = auth.uploadToken(this.appSettingConfigs.getQiniuConfigs().getBucketName(),null,expireTime,null);
        return upToken;
    }

    public String url(String key) {
        if (key == null || key.length()<=0) return "";
        if (key.toLowerCase().startsWith("http://") || key.toLowerCase().startsWith("https://")) {
            return key;
        }
        String encodedFileName = key;
        String publicUrl = String.format("%s/%s", this.appSettingConfigs.getQiniuConfigs().getBucketDomain(), encodedFileName);
        return publicUrl;
    }

    public FileInfo getFileInfo(String key) {
        Configuration cfg = new Configuration(Zone.zone0());
        Auth auth = Auth.create(this.appSettingConfigs.getQiniuConfigs().getAccessKey(),
                this.appSettingConfigs.getQiniuConfigs().getAccessSecrect());
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            FileInfo fileInfo = bucketManager.stat(this.appSettingConfigs.getQiniuConfigs().getBucketName(), key);
            return fileInfo;
        } catch (QiniuException ex) {
            return null;
        }
    }

    public String getRTMPPublishUrl(String streamName) {
        Client cli = new Client(this.appSettingConfigs.getQiniuConfigs().getAccessKey(),
                this.appSettingConfigs.getQiniuConfigs().getAccessSecrect());
        String url = cli.RTMPPublishURL(this.appSettingConfigs.getQiniuConfigs().getLiveRtmpPublishDomain()
                ,this.appSettingConfigs.getQiniuConfigs().getLiveHubName()
                ,streamName
                ,this.appSettingConfigs.getQiniuConfigs().getLivePublishExpiretime());
        return url;
    }

    public String getRTMPPlayUrl(String streamName) {
        Client cli = new Client(this.appSettingConfigs.getQiniuConfigs().getAccessKey(),
                this.appSettingConfigs.getQiniuConfigs().getAccessSecrect());
        String url = cli.RTMPPlayURL(this.appSettingConfigs.getQiniuConfigs().getLiveRtmpPlayDomain()
                ,this.appSettingConfigs.getQiniuConfigs().getLiveHubName()
                ,streamName);
        return url;
    }

    public String getHLSPlayUrl(String streamName) {
        Client cli = new Client(this.appSettingConfigs.getQiniuConfigs().getAccessKey(),
                this.appSettingConfigs.getQiniuConfigs().getAccessSecrect());
        String url = cli.HLSPlayURL(this.appSettingConfigs.getQiniuConfigs().getLiveHlsPlayDomain()
                ,this.appSettingConfigs.getQiniuConfigs().getLiveHubName(),streamName);
        return url;
    }

    public boolean getLiveStatus(String streamName) {
        Client cli = new Client(this.appSettingConfigs.getQiniuConfigs().getAccessKey(),
                this.appSettingConfigs.getQiniuConfigs().getAccessSecrect());
        Hub hub = cli.newHub(this.appSettingConfigs.getQiniuConfigs().getLiveHubName());
        try {
            Stream stream = hub.get(streamName);
            if (stream == null) return false;
            Stream.LiveStatus status = stream.liveStatus();
            return true;
        } catch (PiliException ex) {
            return false;
        }
    }

    public void disableStream(String streamName,Long livingId){
        Client cli = new Client(this.appSettingConfigs.getQiniuConfigs().getAccessKey(),
                this.appSettingConfigs.getQiniuConfigs().getAccessSecrect());
        Hub hub = cli.newHub(this.appSettingConfigs.getQiniuConfigs().getLiveHubName());
        try {
            Stream stream = hub.get(streamName);
            stream.disable();

        }catch (PiliException e){
            e.printStackTrace();
        }

    }
    public boolean watermark(String key){
        Configuration cfg = new Configuration(Zone.zone0());
        Auth auth = Auth.create(this.appSettingConfigs.getQiniuConfigs().getAccessKey(),
                this.appSettingConfigs.getQiniuConfigs().getAccessSecrect());
        //新建一个OperationManager对象
        OperationManager operater = new OperationManager(auth,cfg);
        //需要添加水印的图片UrlSafeBase64,可以参考http://developer.qiniu.com/code/v6/api/dora-api/av/video-watermark.html
        String pictureurl = UrlSafeBase64.encodeToString("http://dev.hd.res.pet.zwdbj.com/1%20%2849%29.jpg");
        logger.info("水印的图片UrlSafeBase64:"+pictureurl);
        //设置转码操作参数
        String text = UrlSafeBase64.encodeToString("爪子APP");
        String fops = "avthumb/mp4/wmImage/"+pictureurl+"/wmGravity/NorthWest/wmOffsetX/10/wmOffsetY/10/wmText/"+text+"/wmGravityText/NorthWest";
        //设置转码的队列
        String pipeline = "zwdbj001";
        //可以对转码后的文件进行使用saveas参数自定义命名，当然也可以不指定,文件会默认命名并保存在当前空间。
        String urlbase64 = UrlSafeBase64.encodeToString(this.appSettingConfigs.getQiniuConfigs().getBucketName()+":"+key+"1");
        String pfops = fops+ "|saveas/"+ urlbase64;
        logger.info("urlbase64:"+urlbase64);
        //设置pipeline参数
        StringMap params = new StringMap().putWhen("force", 1, true).putNotEmpty("pipeline", pipeline)
                .putNotEmpty("notifyURL","http://dev.api.zwdbj.com/am/api/qiniu/qiuniu/notify");
        try {
            String persistid = operater.pfop(this.appSettingConfigs.getQiniuConfigs().getBucketName(), key, pfops, params);
            //打印返回的persistid
            logger.info("打印返回的persistid:"+persistid);
            return true;
        } catch (QiniuException e) {
            //捕获异常信息
            Response r = e.response;
            // 请求失败时简单状态信息
            logger.info("请求失败时简单状态信息"+r.toString());
            try {
                // 响应的文本信息
                logger.info("响应的文本信息"+r.bodyString());
            } catch (QiniuException e1) {
                //ignore
            }
        }
        return false;
    }

}