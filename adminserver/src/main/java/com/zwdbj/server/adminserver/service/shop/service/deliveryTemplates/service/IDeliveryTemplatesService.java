package com.zwdbj.server.adminserver.service.shop.service.deliveryTemplates.service;

import com.zwdbj.server.adminserver.service.shop.service.deliveryTemplates.model.DeliveryTemplatesModel;
import com.zwdbj.server.basemodel.model.ServiceStatusInfo;

import java.util.List;

public interface IDeliveryTemplatesService {
    List<DeliveryTemplatesModel> findAllDeliveryTemplates();
    ServiceStatusInfo<DeliveryTemplatesModel> getDeliveryTemplatesById(long id);
    ServiceStatusInfo<Integer> addDeliveryTemplates(DeliveryTemplatesModel model);
    ServiceStatusInfo<Integer> deleteDeliveryTemplatesById(long id);
    ServiceStatusInfo<Integer> updateDeliveryTemplates(DeliveryTemplatesModel model);
}
