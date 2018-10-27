package com.zwdbj.server.adminserver.easemob.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zwdbj.server.adminserver.config.AppConfigConstant;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class EaseMobTokenManager {
    @Autowired
    private RedisTemplate redisTemplate;
    private String easeMobTokenCacheKey = "easemob_token";
    protected final OkHttpClient client = new OkHttpClient();
    static Logger logger = LoggerFactory.getLogger(EaseMobTokenManager.class);

    public EaseMobToken token() {
        boolean isGetToken = false;
        if (this.redisTemplate.hasKey(easeMobTokenCacheKey)) {
            EaseMobToken easeMobToken = (EaseMobToken)this.redisTemplate.opsForValue().get(easeMobTokenCacheKey);
            if (easeMobToken!=null) {
                return easeMobToken;
            }
            isGetToken = true;
        } else {
            isGetToken = true;
        }
        if (isGetToken) {
            EaseMobToken token = tokenByHttp();
            this.redisTemplate.opsForValue().set(easeMobTokenCacheKey,token,token.getExpires_in()-600,TimeUnit.SECONDS);
            return token;
        }
        return null;
    }

    public EaseMobToken tokenByHttp() {
        String url = String.format("%s/%s/%s/token",
                AppConfigConstant.EASEMOB_HTTP_BASE,
                AppConfigConstant.EASEMOB_ORG_NAME,
                AppConfigConstant.EASEMOB_APP_NAME);
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(MediaType.parse("application/json"),
                        "{\"grant_type\": \"client_credentials\"," +
                                "\"client_id\": \""+AppConfigConstant.EASEMOB_CLIENTID+"\"," +
                                "\"client_secret\": \""+AppConfigConstant.EASEMOB_CLIENTSECRECT+"\"}"))
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String bodyJSON = response.body().string();
                JSONObject jsonObject = JSON.parseObject(bodyJSON);
                EaseMobToken token = new EaseMobToken();
                token.setAccess_token(jsonObject.getString("access_token"));
                token.setExpires_in(jsonObject.getLong("expires_in"));
                token.setApplication(jsonObject.getString("application"));
                return token;
            } else {
                logger.error("获取环信TOKEN失败:>>"+response.message());
                return null;
            }
        } catch (Exception ex) {
            logger.error("获取环信TOKEN失败:>>"+ex.getMessage());
            return null;
        }

    }

}
