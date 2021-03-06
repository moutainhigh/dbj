package com.zwdbj.server.adminserver.service.shop.service.offlineStoreExtraServices.service;

import com.zwdbj.server.adminserver.middleware.mq.ESUtil;
import com.zwdbj.server.adminserver.service.shop.service.offlineStoreExtraServices.mapper.OfflineStoreExtraServicesMapper;
import com.zwdbj.server.adminserver.service.shop.service.offlineStoreExtraServices.model.OfflineStoreExtraServices;
import com.zwdbj.server.basemodel.model.ServiceStatusInfo;
import com.zwdbj.server.utility.common.UniqueIDCreater;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service

public class OfflineStoreExtraServicesServiceImpl implements OfflineStoreExtraServicesService {
    private Logger logger = LoggerFactory.getLogger(OfflineStoreExtraServicesServiceImpl.class);
    @Resource
    private OfflineStoreExtraServicesMapper mapper;
    @Autowired
    private ESUtil esUtil;

    @Transactional
    @Override
    public ServiceStatusInfo<Long> create(OfflineStoreExtraServices offlineStoreExtraServices) {

        Long result = 0L;
        Long id = UniqueIDCreater.generateID();
        result = mapper.create(id, offlineStoreExtraServices);
        esUtil.QueueWorkInfoModelSend(offlineStoreExtraServices.getStoreId(), "shop", "u");
        return new ServiceStatusInfo<>(0, "", result);

    }

    @Transactional
    @Override
    public ServiceStatusInfo<Long> update(OfflineStoreExtraServices offlineStoreExtraServices) {
        Long result = 0L;

        result = mapper.update(offlineStoreExtraServices);
        esUtil.QueueWorkInfoModelSend(offlineStoreExtraServices.getStoreId(), "shop", "u");
        return new ServiceStatusInfo<>(0, "", result);


    }

    @Transactional
    @Override
    public ServiceStatusInfo<Long> deleteById(Long extraServiceId, long storeId) {
        Long result = 0L;

        result = mapper.deleteById(extraServiceId, storeId);
        esUtil.QueueWorkInfoModelSend(storeId, "shop", "u");
        return new ServiceStatusInfo<>(0, "", result);


    }

    @Override
    public ServiceStatusInfo<List<OfflineStoreExtraServices>> select() {
        List<OfflineStoreExtraServices> result = null;
        try {
            result = mapper.select();
            return new ServiceStatusInfo<>(0, "", result);
        } catch (Exception e) {
            return new ServiceStatusInfo<>(1, "查询所有门店其他服务失败" + e.getMessage(), result);
        }

    }

    @Override
    public ServiceStatusInfo<List<OfflineStoreExtraServices>> selectByofflineStoreId(Long offlineStoreId) {
        List<OfflineStoreExtraServices> result = null;
        try {
            result = mapper.selectByofflineStoreId(offlineStoreId);
            return new ServiceStatusInfo<>(0, "", result);
        } catch (Exception e) {
            return new ServiceStatusInfo<>(1, "通过id查询门店其他服务失败" + e.getMessage(), result);
        }
    }

}
