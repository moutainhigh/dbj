package com.zwdbj.server.mobileapi.service.user.mapper;

import com.zwdbj.server.mobileapi.service.user.model.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public class UserSqlProvider {
    private final String TBL_NAME = "core_users";

    public String updateField(Map paramas) {
        Long id = (Long)paramas.get("id");
        String fields = (String)paramas.get("fields");
        SQL sql = new SQL()
                .UPDATE("core_users")
                .SET(fields)
                .WHERE("id=#{id}");
        return sql.toString();
    }


    public String updateInfo(Map params) {
        UpdateUserInfoInput input = (UpdateUserInfoInput)params.get("input");
        long userId = (long)params.get("userId");
        SQL sql = new SQL()
                .UPDATE(TBL_NAME);
        if (input.getAvatarKey()!=null&&input.getAvatarKey().length()>0) {
            sql.SET("avatarUrl=#{input.avatarKey}");
        }
        if (input.getNickName()!=null){
            sql.SET("nickName=#{input.nickName}");
        }
        sql.SET("sex=#{input.sex}");
        if(input.getBirthday()!=null){
            sql.SET("birthday=#{input.birthday}") ;
        }
        if (input.getCity()!=null){
            sql.SET("address=#{input.city}");
        }
        sql.SET("longitude=#{input.longitude}")
        .SET("latitude=#{input.latitude}")
        .SET("occupationId=#{input.occupationId}")
        .SET("loveStatusId=#{input.loveStatusId}");
        if (input.getUserName() != null) {
            sql.SET("username=#{input.userName}");
        }
        sql.WHERE("id=#{userId}");
        return sql.toString();
    }

    public String selectUserAvatarUrl(Map map){
        List<Long> userIds = (List<Long>) map.get("userIds");
        SQL sql = new SQL().SELECT("avatarUrl").FROM(TBL_NAME);
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < userIds.size(); i++) {
            stringBuffer.append("id="+userIds.get(i));
            if(i+1 != userIds.size())stringBuffer.append(" or ");
        }
        sql.WHERE(stringBuffer.toString());
        System.out.println(sql.toString());
        return sql.toString();
    }

}
