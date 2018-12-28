﻿using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace db.shop.Models.shop
{
    [Table("shop_productOrders")]
    public class ProductOrder:db.common.BaseModelWithTime<long>
    {
        /// <summary>
        /// 实付金额，单位为分
        /// </summary>
        /// <value>The payment.</value>
        public int payment { get; set; }
        /// <summary>
        /// 支付类型，BANK:银行卡WECHAT:微信ALIPAY:支付宝
        /// </summary>
        /// <value>The type of the payment.</value>
        [MaxLength(128)]
        [Required]
        public string paymentType { get; set; }
        /// <summary>
        /// 邮费，单位分
        /// </summary>
        /// <value>The delivery fee.</value>
        public int deliveryFee { get; set; }
        /// <summary>
        /// 交易状态
        /// STATE_WAIT_BUYER_PAY（交易创建，等待买家付款）
        /// STATE_BUYER_PAYED (买家已付款)
        /// STATE_SELLER_DELIVERIED（卖家已发货，等待买家收货)
        /// STATE_BUYER_DELIVERIED（买家已确认收货)
        /// STATE_REFUNDING（退款中)
        /// STATE_SUCCESS（交易成功)
        /// STATE_CLOSED(交易关闭)
        /// </summary>
        /// <value>The status.</value>
        [Required]
        [MaxLength(128)]
        public String status { get; set; }
        /// <summary>
        /// 交易更新时间
        /// </summary>
        /// <value>The update time.</value>
        [Column("updateTime", TypeName = "timestamp")]
        public DateTime? updateTime { get; set; }
        /// <summary>
        /// 付款时间
        /// </summary>
        /// <value>The payment time.</value>
        [Column("paymentTime", TypeName = "timestamp")]
        public DateTime? paymentTime { get; set; }
        /// <summary>
        /// 发货时间
        /// </summary>
        /// <value>The payment time.</value>
        [Column("deliveryTime", TypeName = "timestamp")]
        public DateTime? deliveryTime { get; set; }
        /// <summary>
        /// 交易结束时间
        /// </summary>
        /// <value>The end time.</value>
        [Column("endTime", TypeName = "timestamp")]
        public DateTime? endTime { get; set; }
        /// <summary>
        /// 交易关闭时间
        /// </summary>
        /// <value>The close time.</value>
        [Column("closeTime", TypeName = "timestamp")]
        public DateTime? closeTime { get; set; }
        /// <summary>
        /// 物流名字
        /// </summary>
        /// <value>The name of the delivery.</value>
        [MaxLength(128)]
        public String deliveryName { get; set; }
        /// <summary>
        /// 物流类型，主要是物流公司
        /// </summary>
        /// <value>The type of the delivery.</value>
        [MaxLength(50)]
        public String deliveryType { get; set; }
        /// <summary>
        /// 物流单号
        /// </summary>
        /// <value>The delivery code.</value>
        [MaxLength(128)]
        public String deliveryCode { get; set; }
        public long userId { get; set; }
        /// <summary>
        /// 店铺Id
        /// </summary>
        /// <value>The store identifier.</value>
        public long storeId { get; set; }
        /// <summary>
        /// 买家留言
        /// </summary>
        /// <value>The buyer comment.</value>
        [MaxLength(512)]
        public String buyerComment { get; set; }
        /// <summary>
        /// 买家是否已评价
        /// </summary>
        /// <value><c>true</c> if buyer rate; otherwise, <c>false</c>.</value>
        public bool buyerRate { get; set; }
        /// <summary>
        /// 收货地址
        /// </summary>
        /// <value>The receive address identifier.</value>
        public long receiveAddressId { get; set; }
    }
}
