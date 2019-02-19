package com.zwdbj.server.adminserver.service.shop.service.offlineStoreStaffs.service;

import com.zwdbj.server.adminserver.service.shop.service.offlineStoreStaffs.mapper.OfflineStoreStaffsMapper;
import com.zwdbj.server.adminserver.service.shop.service.offlineStoreStaffs.model.*;
import com.zwdbj.server.adminserver.service.shop.service.store.service.StoreServiceImpl;
import com.zwdbj.server.adminserver.service.user.mapper.IUserMapper;
import com.zwdbj.server.adminserver.service.user.service.UserService;
import com.zwdbj.server.utility.common.UniqueIDCreater;
import com.zwdbj.server.utility.model.ServiceStatusInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class OfflineStoreStaffsServiceImpl implements OfflineStoreStaffsService {

    @Resource
    private OfflineStoreStaffsMapper mapper;
    @Resource
    UserService userService;
    @Resource
    StoreServiceImpl storeServiceImpl;
    @Resource
    private IUserMapper iUserMapper;

    @Override
    public ServiceStatusInfo<Long> create(StaffInput staffInput, long legalSubjectId) {

        try {
            long tenantId = storeServiceImpl.selectTenantId(legalSubjectId);
            userService.greateUserByTenant(staffInput.getNickName(), staffInput.getPhone(), tenantId, staffInput.isSuper());
            if (staffInput.isSuperStar()) {
                long userId = iUserMapper.findUserIdByPhone(staffInput.getPhone());
                mapper.setSuperStar(UniqueIDCreater.generateID(), legalSubjectId, userId);
            }

            return new ServiceStatusInfo<>(0, "", 1L);
        } catch (Exception e) {
            return new ServiceStatusInfo<>(1, "创建门店员工/代言人失败" + e.getMessage(), 0L);
        }
    }

    @Override
    public ServiceStatusInfo<Long> update(ModifyStaff modifyStaff, long legalSubjectId) {
        Long result = 0L;
        try {
            if (iUserMapper.userIdIsExist(modifyStaff.getUserId()) == 0) {
                return new ServiceStatusInfo<>(1, "该用户不存在", 0L);
            }
            if (iUserMapper.findUserIdByPhone(modifyStaff.getPhone()) != 0) {
                return new ServiceStatusInfo<>(1, "该手机号已经存在", 0L);
            }

            result = iUserMapper.updateStaffInfo(modifyStaff);

            return new ServiceStatusInfo<>(0, "", result);
        } catch (Exception e) {
            return new ServiceStatusInfo<>(1, "修改门店代言人失败" + e.getMessage(), result);
        }
    }

    /**
     * 删除员工或代言人
     *
     * @param userId
     * @param legalSubjectId
     * @param isSuperStar
     * @return
     */
    @Override
    public ServiceStatusInfo<Long> deleteById(long userId, long legalSubjectId, boolean isSuperStar) {
        Long result = 0L;
        try {

            result = mapper.cancelStaff(userId);
            if (isSuperStar) {
                result += mapper.cancelSuperStar(userId, legalSubjectId);
            }
            return new ServiceStatusInfo<>(0, "", result);
        } catch (Exception e) {
            return new ServiceStatusInfo<>(1, "删除门店员工/代言人失败" + e.getMessage(), result);
        }
    }

    public ServiceStatusInfo<Long> bulkDeleteStaffs(long[] userIds, long legalSubjectId, boolean isSuperStar) {
        Long result = 0L;
        try {
            for (long userId : userIds) {
                result += deleteById(userId, legalSubjectId, isSuperStar).getData();
            }
            return new ServiceStatusInfo<>(0, "", result);
        } catch (Exception e) {
            return new ServiceStatusInfo<>(1, "删除门店员工/代言人失败" + e.getMessage(), result);
        }
    }

    @Override
    public ServiceStatusInfo<List<OfflineStoreStaffs>> getStaffs(long legalSubjectId) {
        List<OfflineStoreStaffs> result = null;
        try {
            result = mapper.getStaffs(legalSubjectId);
            for (OfflineStoreStaffs o : result) {
                Date createTime = mapper.selectSuperStarCreateTime(legalSubjectId, o.getUserId());
                if (createTime != null) {
                    o.setCreateTime(createTime);
                    o.setSuperStar(true);
                }
            }
            return new ServiceStatusInfo<>(0, "", result);
        } catch (Exception e) {
            return new ServiceStatusInfo<>(1, "通过店铺id查询门店员工或代言人失败" + e.getMessage(), result);
        }
    }


    public ServiceStatusInfo<List<OfflineStoreStaffs>> searchStaffs(SearchStaffInfo searchStaffInfo, long legalSubjectId) {
        List<OfflineStoreStaffs> result = new ArrayList<>();
        try {
            if (searchStaffInfo.isSuperStar()) {
                result = mapper.searchSuperStar(legalSubjectId, searchStaffInfo.getSearch());

            }
            result = mapper.searchStaffs(legalSubjectId, searchStaffInfo.getSearch());
            return new ServiceStatusInfo<>(0, "", result);
        } catch (Exception e) {
            return new ServiceStatusInfo<>(1, "搜索失败" + e.getMessage(), null);
        }


    }

    public ServiceStatusInfo<Long> setSuperStar(IsSuperStar isSuperStar, long legalSubjectId) {
        Long result = 0L;
        try {
            if (isSuperStar.isSuperStar()) {
                long id = UniqueIDCreater.generateID();
                result = mapper.setSuperStar(id, legalSubjectId, isSuperStar.getUserId());
                return new ServiceStatusInfo<>(0, "", result);
            }
            result = mapper.cancelSuperStar(isSuperStar.getUserId(), legalSubjectId);
            return new ServiceStatusInfo<>(0, "", result);
        } catch (Exception e) {
            return new ServiceStatusInfo<>(1, "设置/取消代言人失败" + e.getMessage(), result);
        }

    }

    public ServiceStatusInfo<Long> bulkSetSuperStar(IsSuperStar[] isSuperStars, long legalSubjectId) {
        Long result = 0L;
        try {
            for (IsSuperStar isSuperStar : isSuperStars) {
                result += setSuperStar(isSuperStar, legalSubjectId).getData();
            }
            return new ServiceStatusInfo<>(0, "", result);
        } catch (Exception e) {
            return new ServiceStatusInfo<>(1, "设置/取消代言人失败" + e.getMessage(), result);
        }
    }


}
