﻿using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace db.shop.Models.shop
{
    /// <summary>
    /// 商品属性规格名
    /// </summary>
    [Table("shop_productAttris")]
    public class ProductAttri:db.common.BaseModelWithTime<long>
    {
        [Required]
        [MaxLength(30)]
        public String name { get; set; }
        /// <summary>
        /// 属性标识
        /// </summary>
        /// <value>The identify identifier.</value>
        [MaxLength(30)]
        public String identifyId { get; set; }
        /// <summary>
        /// 商品分类
        /// </summary>
        /// <value>The category identifier.</value>
        public long categoryId { get; set; }
        /// <summary>
        /// 父属性
        /// </summary>
        /// <value>The parent identifier.</value>
        public long parentId { get; set; }
        /// <summary>
        /// 属性分组
        /// </summary>
        /// <value>The group identifier.</value>
        public long groupId { get; set; }
    }
}
