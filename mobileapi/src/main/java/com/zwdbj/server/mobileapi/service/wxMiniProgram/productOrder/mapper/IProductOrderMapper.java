package com.zwdbj.server.mobileapi.service.wxMiniProgram.productOrder.mapper;

import com.zwdbj.server.mobileapi.service.wxMiniProgram.productOrder.model.AddOrderInput;
import com.zwdbj.server.mobileapi.service.wxMiniProgram.productOrder.model.OrderOut;
import com.zwdbj.server.mobileapi.service.wxMiniProgram.receiveAddress.model.ReceiveAddressModel;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface IProductOrderMapper {

    @Insert("insert into shop_productOrders(id,orderNo,payment,actualPayment,useCoin,paymentType,thirdPaymentTradeNo," +
            "deliveryFee,status,statusStr,updateTime,paymentTime,userId,storeId,buyerComment,buyerRate,receiveAddressId,receiveAddress," +
            "receiverName,receiverMobile) " +
            "values(#{id},#{id},#{payment},0,#{input.useCoin},'NONE','NONE',#{input.deliveryFee},'STATE_BUYER_PAYED','待发货'," +
            "now(),now(),#{userId},#{input.storeId},#{input.buyerComment},0,#{input.receiveAddressId},#{receiveAddress}," +
            "#{receiverName},#{receiverMobile})")
    int createOrder(@Param("id")long id,@Param("userId")long userId, @Param("input")AddOrderInput input,
                    @Param("payment")int payment,@Param("receiveAddress") String receiveAddress,
                    @Param("receiverName") String receiverName,@Param("receiverMobile") String receiverMobile);
    @Insert("insert into shop_productOrderItems(id,productId,productskuId,orderId,num,title,price,totalFee) " +
            "values(#{id},#{input.productId},#{input.productskuId},#{orderId},#{input.num},#{input.title},#{price},#{totalFee})")
    int createOrderItem(@Param("id")long id,@Param("orderId")long orderId,@Param("input")AddOrderInput input,
                        @Param("price")int price,@Param("totalFee")int totalFee);
    @Select("select ifNull(sum(oi.num),0) from shop_productOrderItems oi left join shop_productOrders o on o.id=oi.orderId " +
            "where o.userId=#{userId} and oi.productId=#{productId} ")
    int userBuyProductAccounts(@Param("userId")long userId,@Param("productId")long productId);

    /**
     * 查询购买商品人的集合
     * @param productId
     * @return
     */
    @Select("select o.userId from shop_productOrderItems oi left join shop_productOrders o on o.id=oi.orderId where  oi.productId=#{productId}")
    List<Long> selectByOrder(@Param("productId") long productId);

    /**
     * TODO 目前针对单个SKU
     * 查询我的兑换
     * @return
     */
    @Select("SELECT oi.productId,p.imageUrls,oi.price,o.payment,o.`status`,o.statusStr,o.endTime,o.actualPayment,o.receiveAddress," +
            "o.receiverName,o.receiverMobile," +
            "o.storeId,oi.title,o.createTime,o.updateTime,o.paymentTime,o.deliveryTime,o.closeTime,o.orderNo,oi.productskuId " +
            "FROM  shop_productOrderItems oi " +
            "LEFT JOIN shop_productOrders o ON o.id = oi.orderId " +
            "LEFT JOIN shop_products p ON oi.productId=p.id " +
            "where o.userId=#{userId} and o.isDeleted=0 and o.actualPayment=0 " +
            "ORDER BY oi.createTime DESC")
    List<OrderOut> selectMyOrder(@Param("userId")long userId);
}
