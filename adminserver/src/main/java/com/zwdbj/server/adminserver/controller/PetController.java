package com.zwdbj.server.adminserver.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zwdbj.server.adminserver.model.EntityKeyModel;
import com.zwdbj.server.adminserver.model.ResponseData;
import com.zwdbj.server.adminserver.model.ResponseDataCode;
import com.zwdbj.server.adminserver.model.ResponsePageInfoData;
import com.zwdbj.server.adminserver.service.ServiceStatusInfo;
import com.zwdbj.server.adminserver.service.pet.model.PetModelDto;
import com.zwdbj.server.adminserver.service.pet.model.UpdatePetModelInput;
import com.zwdbj.server.adminserver.service.pet.service.PetService;
import com.zwdbj.server.adminserver.shiro.JWTUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(description = "宠物相关")
@RequestMapping("/api/pet")
@RestController
public class PetController {
    @Autowired
    PetService petService;

    @RequestMapping(value = "/list/{userId}",method = RequestMethod.GET)
    @RequiresAuthentication
    @ApiOperation(value = "获取我的宠物")
    public ResponsePageInfoData<List<PetModelDto>> list(@RequestParam(value = "pageNo",required = true,defaultValue = "1") int pageNo,
                                                        @RequestParam(value = "rows",required = true,defaultValue = "30") int rows,
                                                        @PathVariable long userId) {
        Page<PetModelDto> pageInfo = PageHelper.startPage(pageNo,rows);
        List<PetModelDto> pets = this.petService.list(userId);
        return new ResponsePageInfoData<>(ResponseDataCode.STATUS_NORMAL,"",pets, pageInfo.getTotal());
    }
    @RequestMapping(value = "/findMore",method = RequestMethod.POST)
    @RequiresAuthentication
    @ApiOperation(value = "查询多个宠物的数据")
    public ResponseData<List<PetModelDto>> findMore(@RequestBody List<EntityKeyModel<Long>> ids) {
        List<PetModelDto> pets = this.petService.findMore(ids);
        return new ResponseData<>(ResponseDataCode.STATUS_NORMAL,"",pets);
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    @RequiresAuthentication
    @ApiOperation(value = "获取宠物信息")
    public ResponseData<PetModelDto> get(@PathVariable long id) {
        PetModelDto petModelDto = this.petService.get(id);
        if (petModelDto ==null) {
            return new ResponseData<>(ResponseDataCode.STATUS_NOT_FOUND,"未找到",null);
        }
        return new ResponseData<>(ResponseDataCode.STATUS_NORMAL,"",petModelDto);
    }

    @RequiresAuthentication
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @ApiOperation(value = "删除宠物")
    public ResponseData<Object> delete(@RequestBody EntityKeyModel<Long> input) {
        ServiceStatusInfo<Long> statusInfo = this.petService.delete(input.getId());
        if (statusInfo.isSuccess()) {
            return new ResponseData<>(ResponseDataCode.STATUS_NORMAL,"",null);
        } else {
            return new ResponseData<>(ResponseDataCode.STATUS_ERROR,statusInfo.getMsg(),null);
        }
    }

    @RequiresAuthentication
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @ApiOperation(value = "保存宠物信息")
    public ResponseData<Object> save(@RequestBody UpdatePetModelInput input) {
        long userId = JWTUtil.getCurrentId();
        ServiceStatusInfo<Long> statusInfo = null;
        if(input.getId()==0) {
            statusInfo = this.petService.add(input,userId);
        } else {
            statusInfo = this.petService.update(input);
        }
        if (statusInfo.isSuccess()) {
            return new ResponseData<>(ResponseDataCode.STATUS_NORMAL,"",null);
        } else {
            return new ResponseData<>(ResponseDataCode.STATUS_ERROR,statusInfo.getMsg(),null);
        }
    }

}
