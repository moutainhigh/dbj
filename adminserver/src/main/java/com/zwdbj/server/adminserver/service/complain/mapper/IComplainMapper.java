package com.zwdbj.server.adminserver.service.complain.mapper;

import com.zwdbj.server.adminserver.service.complain.model.*;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface IComplainMapper {

    @SelectProvider(type = ComplainSqlProvider.class,method = "basicComplainAd")
    List<AdComplainListDto> basicCompalinAd(@Param("input")AdFindComplainInput input,@Param("resTypeId")int resTypeId);

    @Insert("insert into core_complainReasons(id,title,description,type) " +
            "values(#{id},#{input.title},#{input.description},#{type})")
    Long addComplainAd(@Param("input")AdNewComplainInput input,
                       @Param("id")Long id,
                       @Param("type")int type);

    @Select("SELECT toResId, COUNT(id) complainCount FROM core_complains WHERE toResTypeId=0 GROUP BY toResId")
    List<UserComplainDto> findUserComplainCount();
}
