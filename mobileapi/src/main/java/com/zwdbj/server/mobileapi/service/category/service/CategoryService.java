package com.zwdbj.server.mobileapi.service.category.service;

import com.zwdbj.server.mobileapi.service.category.mapper.ICategoryMapper;
import com.zwdbj.server.mobileapi.service.category.model.*;
import com.zwdbj.server.utility.model.ServiceStatusInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CategoryService {
    @Autowired
    ICategoryMapper categoryMapper;

    @Autowired
    RedisTemplate redisTemplate;

    public List<CategoryDto> search(CategorySearchInput input) {
        List<CategoryDto> dtos = this.categoryMapper.search(input);
        return dtos;
    }

    public ServiceStatusInfo<List<CategoryMainDto>> mainSelect(){
        try{
            List<CategoryMainDto> categoryMainDtos;
            //TODO 未更新数据
            if(redisTemplate.hasKey("MAINCATEGORY")){
                categoryMainDtos = (List<CategoryMainDto>) redisTemplate.opsForValue().get("MAINPRODUCT");
                return new ServiceStatusInfo<>(0,"",categoryMainDtos);
            }
            categoryMainDtos = this.categoryMapper.mainSelect(0);
            for (CategoryMainDto categoryMainDto: categoryMainDtos) {
                categoryMainDto.setCategoryMainDtoList(categoryMapper.mainSelect(categoryMainDto.getId()));
            }
            redisTemplate.opsForValue().set("MAINCATEGORY",categoryMainDtos);
            return new ServiceStatusInfo<>(0,"",categoryMainDtos);
        }catch(Exception e){
            return new ServiceStatusInfo<>(1,e.getMessage(),null);
        }
    }



}
