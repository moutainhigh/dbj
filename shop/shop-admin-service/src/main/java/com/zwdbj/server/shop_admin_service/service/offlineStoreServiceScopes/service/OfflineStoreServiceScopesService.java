package com.zwdbj.server.shop_admin_service.service.offlineStoreServiceScopes.service;

import com.zwdbj.server.shop_admin_service.service.offlineStoreServiceScopes.model.OfflineStoreServiceScopes;
import com.zwdbj.server.utility.model.ServiceStatusInfo;

import java.util.List;

public interface OfflineStoreServiceScopesService {

    ServiceStatusInfo<Long> create(OfflineStoreServiceScopes offlineStoreServiceScopes);

    ServiceStatusInfo<Long> update(OfflineStoreServiceScopes offlineStoreServiceScopes);

    ServiceStatusInfo<Long> deleteById(Long id);

    ServiceStatusInfo<List<OfflineStoreServiceScopes>> select();

    ServiceStatusInfo< List<OfflineStoreServiceScopes>> selectByofflineStoreId(Long offlineStoreId);


}