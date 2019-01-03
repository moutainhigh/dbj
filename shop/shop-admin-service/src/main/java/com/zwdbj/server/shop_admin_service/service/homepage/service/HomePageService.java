package com.zwdbj.server.shop_admin_service.service.homepage.service;

import com.zwdbj.server.shop_admin_service.service.homepage.model.OrderTrend;
import com.zwdbj.server.shop_admin_service.service.homepage.model.TodayOverview;
import com.zwdbj.server.utility.model.ServiceStatusInfo;

import java.util.HashMap;
import java.util.List;

public interface HomePageService {
    ServiceStatusInfo<TodayOverview> select(long sellerId);

    ServiceStatusInfo<HashMap<String, List<OrderTrend>>> selectOrderDayTrend(long sellerId);

    ServiceStatusInfo<HashMap<String, List<OrderTrend>>> selectOrderWeekTrend(long sellerId);

//    ServiceStatusInfo<List<VideoTrend>> selectVideoDayTrend(long sellerId);
//
//    ServiceStatusInfo<List<VideoTrend>> selectVideoWeekTrend(long sellerId);

}
