package com.zwdbj.server.adminserver.service.dailyIncreaseAnalysises.mapper;

import com.zwdbj.server.adminserver.service.homepage.model.AdFindIncreasedInput;
import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

public class DailyincreaseaSqlProvider {
    public String userGrowthAd(Map params){
        AdFindIncreasedInput input = (AdFindIncreasedInput)params.get("input");
        SQL sql = new SQL()
                .SELECT("createTime,newUsers as growthed")
                .FROM("core_dailyIncreaseAnalysises");
        if (input.getQuantumTime()==1){
            sql.WHERE("YEARWEEK(DATE_FORMAT(createTime,'%Y-%m-%d'))=YEARWEEK(NOW()) AND date(createTime)<=curDate()-1");
        }else if (input.getQuantumTime()==2){
            sql.WHERE("DATE_FORMAT(createTime,'%Y-%m')=DATE_FORMAT(NOW(),'%Y-%m') AND DATE(createTime)<=curDate()-1");
        }
        sql.ORDER_BY("createTime");
     return sql.toString();
    }

    public String videoGrowthAd(Map params){
        AdFindIncreasedInput input = (AdFindIncreasedInput)params.get("input");
        SQL sql = new SQL()
                .SELECT("createTime,newVideos as growthed")
                .FROM("core_dailyIncreaseAnalysises");
        if (input.getQuantumTime()==1){
            sql.WHERE("YEARWEEK(DATE_FORMAT(createTime,'%Y-%m-%d'))=YEARWEEK(NOW()) AND date(createTime)<=curDate()-1");
        }else if (input.getQuantumTime()==2){
            sql.WHERE("DATE_FORMAT(createTime,'%Y-%m')=DATE_FORMAT(NOW(),'%Y-%m') AND DATE(createTime)<=curDate()-1");
        }
        sql.ORDER_BY("createTime");
        return sql.toString();
    }
}
