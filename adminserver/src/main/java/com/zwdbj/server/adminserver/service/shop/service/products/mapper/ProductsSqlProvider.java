package com.zwdbj.server.adminserver.service.shop.service.products.mapper;

import com.zwdbj.server.adminserver.service.shop.service.products.model.SearchProducts;
import org.apache.ibatis.jdbc.SQL;

import java.util.Date;
import java.util.Map;

public class ProductsSqlProvider {

    public String search(Map paras) {
        SearchProducts searchProduct = (SearchProducts) paras.get("searchProducts");
        SQL sql = new SQL()
                .SELECT("*")
                .FROM("shop_products");
        if (searchProduct.getName() != null) {
            sql.WHERE("name='" + searchProduct.getName()+"'");
        } else if (searchProduct.getNumberId() != null) {
            sql.WHERE("numberId='" + searchProduct.getNumberId()+"'");
        } else if (searchProduct.getPriceDown() != 0) {
            sql.WHERE("priceDown>=" + searchProduct.getPriceDown());
        } else if (searchProduct.getPriceUp() != 0) {
            sql.WHERE("priceUp<=" + searchProduct.getPriceUp());
        } else if (searchProduct.getProductGroupId() != 0) {
            sql.WHERE("productGroupId=" + searchProduct.getProductGroupId());
        } else if (searchProduct.getProductType() != 0) {
            sql.WHERE("productType=" + searchProduct.getProductType());
        } else if (searchProduct.getSalesUp() != 0) {
            sql.WHERE("salesUp<=" + searchProduct.getSalesUp());
        } else if (searchProduct.getSalseDown() != 0) {
            sql.WHERE("salesDown>=" + searchProduct.getSalseDown());
        }
        sql.ORDER_BY("createTime");
        return sql.toString();
    }

    /**
     * 根据条件查询
     * @param paras
     * @return
     */
    public String searchCondition(Map paras) {
        SearchProducts searchProduct = (SearchProducts) paras.get("searchProducts");
        int type= (int) paras.get("type");
        Date date = new Date();
        SQL sql = new SQL()
                .SELECT("*")
                .FROM("shop_products");
        if(type==1){
            //销售中
            sql.WHERE("(publish = 1 or (publish = 0 and specifyPublishTime!=0 and specifyPublishTime < "+date.getTime()+")) and inventory != 0");
        }else if(type==2){
            //已售完
            sql.WHERE("(publish = 1 or (publish = 0 and specifyPublishTime!=0 and specifyPublishTime < "+date.getTime()+")) and inventory = 0");
        }else if(type==3){
            //待上架
            sql.WHERE("publish = 0 and specifyPublishTime!=0 and specifyPublishTime > "+date.getTime());
        }
        if (searchProduct.getName() != null) {
            sql.WHERE("name like '%" + searchProduct.getName()+"%'");
        }
        if (searchProduct.getNumberId() != null) {
            sql.WHERE("numberId='" + searchProduct.getNumberId()+"'");
        }
        if (searchProduct.getPriceDown() != 0) {
            sql.WHERE("priceDown>=" + searchProduct.getPriceDown());
        }
        if (searchProduct.getPriceUp() != 0) {
            sql.WHERE("priceUp<=" + searchProduct.getPriceUp());
        }
        if (searchProduct.getProductGroupId() != 0) {
            sql.WHERE("productGroupId=" + searchProduct.getProductGroupId());
        }
        if (searchProduct.getProductType() != null) {
            sql.WHERE("productType=" + searchProduct.getProductType());
        }
        if (searchProduct.getSalesUp() != 0) {
            sql.WHERE("salesUp<=" + searchProduct.getSalesUp());
        }
        if (searchProduct.getSalseDown() != 0) {
            sql.WHERE("salesDown>=" + searchProduct.getSalseDown());
        }
        sql.WHERE("storeId="+searchProduct.getStoreId());
        sql.WHERE("isDeleted=0");
        sql.ORDER_BY("createTime desc");
        return sql.toString();
    }

    /**
     * 批量上下架
     * @param map
     * @return
     */
    public String updatePublish(Map map){
        Long[] id = (Long[]) map.get("id");
        Long storeId = (Long) map.get("storeId");
        boolean publish = (boolean) map.get("publish");
        SQL sql = new SQL()
                .UPDATE("shop_products")
                .SET("publish = "+publish);
        if(!publish){
            sql.SET("specifyPublishTime = 0");
        }
        sql.WHERE(stringSqlUtil(id));
        sql.AND();
        sql.WHERE("isDeleted=0");
        sql.WHERE("storeId="+storeId);
        return sql.toString();
    }

    /**
     * 批量删除方法
     * @param map
     * @return
     */
    public String deleteByProducts(Map map){
        Long[] id = (Long[]) map.get("id");
        Long storeId = (Long) map.get("storeId");
        SQL sql = new SQL().UPDATE("shop_products").SET("isDeleted=1").SET("deleteTime=now()");
        sql.WHERE(stringSqlUtil(id));
        sql.AND();
        sql.WHERE("storeId="+storeId);
        return sql.toString();
    }

    public String stringSqlUtil(Long[] id){
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < id.length; i++) {
            stringBuffer.append("id="+id[i]);
            if(i+1 != id.length)stringBuffer.append(" or ");
        }
        return stringBuffer.toString();
    }
}