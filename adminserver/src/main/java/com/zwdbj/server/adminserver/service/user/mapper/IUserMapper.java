package com.zwdbj.server.adminserver.service.user.mapper;

import com.zwdbj.server.adminserver.model.ResourceOpenInput;
import com.zwdbj.server.adminserver.service.homepage.model.AdFindIncreasedDto;
import com.zwdbj.server.adminserver.service.homepage.model.AdFindIncreasedInput;
import com.zwdbj.server.adminserver.service.shop.service.offlineStoreStaffs.model.ModifyStaff;
import com.zwdbj.server.adminserver.service.user.model.*;
import com.zwdbj.server.adminserver.service.user.model.CreateUserInput;
import com.zwdbj.server.adminserver.service.user.model.UserSearchForAdInput;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface IUserMapper {
    @Select("select * from core_users where username=#{username}")
    UserModel findUserByUserName(@Param("username") String username);

    @Select("select * from core_users where id=#{id}")
    UserModel findUserById(@Param("id") long id);

    @Select("select u.*,ur.roleName FROM core_users u INNER JOIN core_userRoles ur ON ur.userId=u.id where username=#{username} and password=#{password}")
    UserModel findUserByUserPwd(@Param("username") String username, @Param("password") String password);

    @Select("select u.*,ur.roleName FROM core_users u INNER JOIN core_userRoles ur ON ur.userId=u.id where u.phone=#{phone} and u.password=#{password} " +
            "and `type` in ('BUSINESS','PLATFORM')")
    UserModel findUserByPhonePwd(@Param("phone") String username, @Param("password") String password);

    @Select("SELECT *, (select count(*) from core_pets as pet where pet.userId = u.id) as petCount," +
            "(select count(*) from core_videos as vd where vd.userId = u.id) as videoCount," +
            "(select count(*) from core_livings as li where li.userId = u.id) as liveCount," +
            "(select ut.nickName from core_userThirdAccountBinds as ut where ut.userId = u.id and ut.accountType=4) as weChatName," +
            "(select ut.nickName from core_userThirdAccountBinds as ut where ut.userId = u.id and ut.accountType=8) as QQName," +
            "(select ut.nickName from core_userThirdAccountBinds as ut where ut.userId = u.id and ut.accountType=16) as weiBoName FROM " +
            "dbj_server_db.core_users as u where u.id=#{userId}")
    UserDetailInfoDto getUserDetail(@Param("userId") long userId);

    @SelectProvider(type = UserSqlProvider.class, method = "queryUser")
    List<UserDetailInfoDto> findUsersAd(@Param("model") UserSearchForAdInput model, @Param("flag") boolean flag);

    @Select("SELECT u.*, fk.`petCount` as petCount,fk.`videoCount` as videoCount  " +
            "FROM `core_users` as u INNER JOIN `data_fake_users` as fk on u.`username` = fk.`username` " +
            "LIMIT #{s},#{e}")
    List<UserDetailInfoDto> findUsersFakeAd(@Param("s") int s, @Param("e") int e);

    @SelectProvider(type = UserSqlProvider.class, method = "marketListAd")
    List<UserDetailInfoDto> marketListAd(@Param("input") AdMarketUserInput input);

    @Insert("insert into core_users(id,username,nickName,fullName,avatarUrl,password,phone,isPhoneVerification,tenantId,notes,`type`) " +
            "values(#{id},#{username},'爪子用户',#{fullName},'http://res.pet.zwdbj.com/default_avatar.png',#{password}," +
            "#{phone},true,#{tenantId},#{notes},#{type})")
    int greateUserByTenant(@Param("id") long id, @Param("username") String username, @Param("password") String password,
                           @Param("fullName") String fullName, @Param("phone") String phone,
                           @Param("tenantId") long tenantId, @Param("notes") String notes,
                           @Param("type")String type);

    @Update("update core_users set isSuper=0 where tenantId=#{tenantId} and isSuper=1")
    int modifyUserByTenantId(@Param("tenantId") long tenantId);
    @Select("select userId from core_userRoles where  tenantId=#{tenantId} and roleName=#{roleName}")
    long findUserByTenId(@Param("tenantId") long tenantId,@Param("roleName")String roleName);

    @Insert("insert into core_users(id,username,nickname,phone,avatarUrl,isSuper,isManager,sex,`type`) " +
            "values(#{userId},#{input.userName},#{input.userName},#{input.phone}," +
            "'http://res.pet.zwdbj.com/default_avatar.png',false,true,#{input.gender},'PLATFORM')")
    Long newMarketAd(@Param("userId") Long userId, @Param("input") AdNewMarketInput input);

    @Insert("insert into core_userRoles(id,userId,roleName,tenantId) values(#{id},#{userId},#{roleName},#{tenantId})")
    Long insertUserRole(@Param("id") Long id, @Param("userId") Long userId, @Param("roleName") String roleName, @Param("tenantId") long tenantId);

    @Insert("insert into core_users(id,phone,username,nickName,avatarUrl,isSuper,password,isManager,`type`) " +
            "values(#{id},'00000000000','admin','admin','http://res.pet.zwdbj.com/default_avatar.png',true,#{password},true,'PLATFORM')")
    long regAdmin(@Param("password") String password, @Param("id") long id);

    @UpdateProvider(type = UserSqlProvider.class, method = "updateField")
    long updateField(@Param("fields") String fields, @Param("id") long id);

    @Update("update core_users set notes=#{modifyStaff.notes} where id=#{modifyStaff.userId}")
    long updateStaffInfo(@Param("modifyStaff") ModifyStaff modifyStaff);

    //举报用户
    @SelectProvider(type = UserSqlProvider.class, method = "searchComplainUserListAd")
    List<UserDetailInfoDto> searchComplainUserListAd(@Param("input") AdUserComplainInput input);

    @Select("select c.*,r.title,r.description,u.nickName,u.userName from core_complains as c " +
            "inner join core_complainReasons r on c.reasonId=r.id " +
            "inner join core_users as u on c.fromUserId =u.id " +
            "where c.toResId=#{toResId} and c.toResTypeId=0")
    List<AdUserComplainInfoDto> userComplainInfoListAd(@Param("toResId") Long toResId);

    @Update("update core_users set isLocked=#{input.isLocked} where id=#{id}")
    Long doUComplainInfoAd(@Param("id") Long id, @Param("input") AdDoUserComplainInput input);

    //账号管理
    @SelectProvider(type = UserSqlProvider.class, method = "manageUserListAd")
    List<AdUserDetailInfoDto> manageUserListAd(@Param("input") AdManageUserInput input);

    @Insert("insert into core_users(id,phone,username,nickName,avatarUrl,password,isSuper,isManager,`type`) " +
            "values(#{id},#{input.phone},#{input.userName},#{input.userName},'http://res.pet.zwdbj.com/default_avatar.png',#{password},false,true,'PLATFORM')")
    Long addManageUserAd(@Param("id") Long id, @Param("input") AdNewManageUserInput input, @Param("password") String password);

    @Insert("insert into core_userRoles(id,userId,roleName) values(#{id},#{userId},#{roleName})")
    Long insertManageUserRole(@Param("id") Long id, @Param("userId") Long userId, @Param("roleName") String roleName);

    @Update("update core_users set password=#{input.mNewPassword} where id=#{id} and password=#{input.oldPassword}")
    Long modifyPwdAd(@Param("id") Long id, @Param("input") AdModifyManagerPasswordInput input);
    @Update("update core_users set password=#{input.mNewPassword} where phone=#{input.phone}")
    Long newlyPwdAd(@Param("input") AdNewlyPwdInput input);

    @Select("select count(*) from core_users where phone=#{phone} and isManualData=0")
    Long phoneIsExist(@Param("phone") String phone);

    @Select("select count(*) from core_users where userName=#{userName}")
    Long userNameIsExist(@Param("userName") String userName);

    @Select("select * from core_users where phone=#{phone}")
    UserModel findUserByPhone(@Param("phone") String phone);

    @Select("select count(*) from core_users where id=#{userId}")
    Long userIdIsExist(@Param("userId") long userId);

    @Select("select id from core_users where phone=#{phone} and isManualData=0")
    long findUserIdByPhone(@Param("phone") String phone);

    @Update("update core_users set tenantId=#{tenantId},notes=#{notes},`type`=#{type} where id=#{id}")
    int updateUserTanById(@Param("id") long id, @Param("tenantId") long tenantId,@Param("notes")String notes,@Param("type")String type);

    @Select("select nickName from core_users where id=#{id}")
    String getNickNameById(@Param("id") long id);

    @Select("select count(id) from core_userRoles where userId=#{userId} and roleName=#{roleName}")
    int findUserRole(@Param("userId") long userId, @Param("roleName") String roleName);

    @Update("update core_userRoles set tenantId=#{tenantId} where userId=#{userId} and roleName=#{roleName}")
    int updateUserRole(@Param("userId") long userId, @Param("roleName") String roleName, @Param("tenantId") long tenantId);

    @Delete("delete from core_userRoles where userId=#{userId} and roleName=#{roleName} and tenantId=#{tenantId}")
    int deleteUserRole(@Param("userId") long userId, @Param("roleName") String roleName, @Param("tenantId") long tenantId);

    //用户认证
    @Update("update core_users set isLivingOpen=#{input.isOpen},isReviewed=#{input.isOpen},isLivingWatch=#{input.isOpen} where id=#{input.id}")
    long review(@Param("input") ResourceOpenInput<Long> input);

    @Update("update core_users set isLocked=#{input.isOpen} where id=#{input.id}")
    long lock(@Param("input") ResourceOpenInput<Long> input);

    //创建用户
    @Insert("insert into core_users(id,phone,username,nickName,avatarUrl,isSuper,password,isManager,`type`) " +
            "values(#{id},#{input.phone},#{input.userName},#{input.name},'http://res.pet.zwdbj.com/default_avatar.png',false,#{password},true,'PLATFORM')")
    long create(@Param("input") CreateUserInput input, @Param("id") long id, @Param("password") String password);

    // 关注
    @Select("select count(*) from core_followers where userId=#{userId} and followerUserId=#{followerUserId}")
    int checkFollowed(@Param("userId") long userId, @Param("followerUserId") long followerUserId);


    //角色
    @Delete("delete from core_userRoles where userId=#{userId}")
    long deleteRole(@Param("userId") long userId);
    //角色
    @Delete("delete from core_userRoles where userId=#{userId} and tenantId=#{tenantId} and roleName in('business_admin','business_user')")
    int deleteBusinessRole(@Param("userId") long userId,@Param("tenantId") long tenantId);
    @Delete("delete from core_userRoles where tenantId=#{tenantId}")
    int deleteAllBusinessRole(@Param("tenantId") long tenantId);

    @Select("select roleName from core_userRoles where userId=#{userId}")
    List<String> roles(@Param("userId") long userId);

    @Select("select c.permissionName from core_users as a inner join core_userRoles as b " +
            "inner join core_rolePermissions as c on a.id = b.userId and b.roleName = c.roleName where a.id=#{userId}")
    List<String> permissions(@Param("userId") long userId);

    @Select("select permissionName from core_rolePermissions where roleName=#{roleName}")
    List<String> permissionsByRole(@Param("roleName") String roleName);

    @Insert("insert into core_userRoles(id,userId,roleName) values(#{id},#{userId},#{roleName})")
    long setRole(@Param("userId") long userId, @Param("roleName") String roleName, @Param("id") long id);

    @Insert("insert into core_rolePermissions(id,permissionName,roleName) values(#{id},#{permissionName},#{roleName})")
    long setPermission(@Param("roleName") String roleName, @Param("permissionName") String permissionName, @Param("id") long id);

    @Insert("insert into core_roles(name,description) values(#{name},#{des})")
    long addRole(@Param("name") String name, @Param("des") String des);

    @Insert("insert into core_permissions(name,description) values(#{name},#{des})")
    long addPermission(@Param("name") String name, @Param("des") String des);


    //首页新增数量

    @SelectProvider(type = UserSqlProvider.class, method = "findIncreasedUserAd")
    AdFindIncreasedDto findIncreasedUserAd(@Param("input") AdFindIncreasedInput input, @Param("flag") boolean flag);

    @Select("select count(id) from core_users where isManualData=0 and DATE_SUB(CURDATE(), INTERVAL 7 DAY) <= date(createTime)")
    long dau();

    @Select("select count(id) from core_users where date(createTime)=curDate()-1 and isManualData=false")
    Long everyIncreasedUsers();

    //审核相关
    @Update("update core_users set avatarUrl='http://res.pet.zwdbj.com/default_avatar.png' where id=#{id}")
    int reviewUserAvatar(@Param("id") Long id);

    //定时任务
    @Select("select userId,count(id) as totalFans FROM core_followers GROUP BY userId")
    List<UserIdAndFollowersDto> findMyFansCount();

    @Select("select followerUserId,count(id) as totalMyFocuses FROM core_followers GROUP BY followerUserId")
    List<UserIdAndFocusesDto> findMyFocusesCount();

    @Insert("insert into core_users(id,phone,username,nickName,avatarUrl,IsPhoneVerification) values(#{id},#{phone}," +
            "#{username},#{nickName},#{avatarUrl},true)")
    long newVestUser(@Param("phone") String phone, @Param("id") long id, @Param("username") String username, @Param("avatarUrl") String avatarUrl, @Param("nickName") String nickName);

    @Select("select id  from core_users where phone like '56%'")
    List<Long> getVestUserIds1();

    @Select("select id  from core_users where phone like '56%' and username='爪子用户'")
    List<Long> getVestUserIds2();
    @Select("select id  from core_users where isManualData=true and isDeleted=0")
    List<Long> getManualUser();

    /**
     * 商家查询员工
     *
     * @param userShopSearchInput
     * @param storeId
     * @param tenantId
     * @return
     */
    @SelectProvider(type = UserSqlProvider.class, method = "selectStaff")
    List<UserShopSelectInput> selectStaff(@Param("userShopSearchInput") UserShopSearchInput userShopSearchInput, @Param("storeId") long storeId, @Param("tenantId") long tenantId);

    @Insert("insert into core_users(id,phone,username,nickName,avatarUrl,isSuper,password,isManager,isPhoneVerification,tenantId) " +
            "values(#{id},#{input.phone},#{input.userName},#{input.name},'http://res.pet.zwdbj.com/default_avatar.png',false,#{password},true,true,#{tenantId})")
    long createUserShop(@Param("input") CreateUserInput input, @Param("id") long id, @Param("password") String password, @Param("tenantId") long tenantId);

    @Update("update core_users set nickName=#{input.name},tenantId=#{tenantId} where phone=#{input.name}")
    long updateUserByPhone(@Param("input") CreateUserInput createUserInput, @Param("tenantId") long tenantId);

    /**
     * 批量删除员工
     *
     * @param id
     * @return
     */
    @UpdateProvider(type = UserSqlProvider.class, method = "deleteByIds")
    long deleteByIds(@Param("id") long[] id, @Param("tenantId") long tenantId);

}
