package com.zwdbj.server.mobileapi.service.wxMiniProgram.setting.service;

import com.zwdbj.server.basemodel.model.ServiceStatusInfo;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class WxMiniProgramService {
    private Logger logger = LoggerFactory.getLogger(WxMiniProgramService.class);
    protected final OkHttpClient client = new OkHttpClient();
    public ServiceStatusInfo<Object> getOpenIdByCode(String code){
        String url = String.format("https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                "wxfffc024e2f801969","d4a4331faf120e95e90c8405d6c49543",code);
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String bodyJSON = response.body().string();
                //JSONObject jsonObject = JSON.parseObject(bodyJSON);
                /*int errcode = jsonObject.getInteger("errcode");
                String msg = jsonObject.getString("errmsg");*/
                //logger.info("errcode:"+errcode+",errmsg:"+msg);
                logger.info(bodyJSON);
                return new ServiceStatusInfo<>(0,"",bodyJSON);
            } else {
                return new ServiceStatusInfo<>(1,response.message(),"");
            }
        }
        catch (Exception ex) {
            return new ServiceStatusInfo<>(1,ex.getMessage(),"");
        }
    }
}
