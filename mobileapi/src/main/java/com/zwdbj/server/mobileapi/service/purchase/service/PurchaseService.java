package com.zwdbj.server.mobileapi.service.purchase.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zwdbj.server.mobileapi.service.purchase.model.ResponseMsg;
import com.zwdbj.server.mobileapi.service.purchase.util.IosVerifyUtil;
import com.zwdbj.server.mobileapi.service.userAssets.model.BuyCoinConfigModel;
import com.zwdbj.server.mobileapi.service.userAssets.model.UserCoinDetailAddInput;
import com.zwdbj.server.mobileapi.service.userAssets.service.IUserAssetService;
import com.zwdbj.server.basemodel.model.ServiceStatusInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PurchaseService {
    @Autowired
    IUserAssetService userAssetServiceImpl;
    private Logger logger = LoggerFactory.getLogger(PurchaseService.class);





    /**
     * @throws Exception
     * 苹果内购支付
     * @Title: doIosRequest
     * @Description:Ios客户端内购支付
     * @param  transactionID ：交易标识符
     * @param  payload：二次验证的重要依据 receipt
     * @throws
     */
    public ServiceStatusInfo<ResponseMsg> doIosRequest(String transactionID, String payload, long userId) throws Exception {
        try {
            ResponseMsg responseMsg = new ResponseMsg();
            logger.info("客户端传过来的值1："+transactionID+"客户端传过来的值2："+payload);

            String verifyResult =  IosVerifyUtil.buyAppVerify(payload,1); 			//1.先线上测试    发送平台验证
            if (verifyResult == null) {   											// 苹果服务器没有返回验证结果
                logger.info("无订单信息!");
            } else {  	    														// 苹果验证有返回结果
                logger.info("线上，苹果平台返回JSON:"+verifyResult);
                JSONObject job = JSONObject.parseObject(verifyResult);
                String states = job.getString("status");

                if("21007".equals(states)){									//是沙盒环境，应沙盒测试，否则执行下面
                    verifyResult =  IosVerifyUtil.buyAppVerify(payload,0);			//2.再沙盒测试  发送平台验证
                    logger.info("沙盒环境，苹果平台返回JSON:"+verifyResult);
                    job = JSONObject.parseObject(verifyResult);
                    states = job.getString("status");
                }
                int status = Integer.valueOf(states);
                responseMsg.setStatus(status);
                responseMsg.setReceipt(verifyResult);

                logger.info("苹果平台返回值：job"+job);
                if (status==0){ // 前端所提供的收据是有效的    验证成功
                    String r_receipt = job.getString("receipt");
                    JSONObject returnJson = JSONObject.parseObject(r_receipt);
                    JSONArray in_appJsons = returnJson.getJSONArray("in_app");
                    int size = in_appJsons.size();
                    logger.info("size:"+size);
                    JSONObject in_appJson = null;
                    String product_id = "";
                    String transaction_id = "";// 订单号
                    for (int i=0;i<size;i++){
                        in_appJson = in_appJsons.getJSONObject(i);
                        product_id = in_appJson.getString("product_id");
                        transaction_id = in_appJson.getString("transaction_id");// 订单号
                        if (transaction_id.equals(transactionID)){
                            logger.info("transaction_id:"+transaction_id);
                            responseMsg.setProduct_id(product_id);
                            responseMsg.setTransactionId(transaction_id);
                            break;
                        }
                    }

 /************************************************+自己的业务逻辑**********************************************************/
                    int a = 0;
                    long id=0L;
                    if(transactionID.equals(transaction_id)){
                        logger.info("*************************我是业务逻辑11111111*************************");
                        //查看数据库是否已经增加该transactionId的金币交易
                        boolean b = this.userAssetServiceImpl.findCoinDetailByTrade(transaction_id,"APPLEPAY");
                        if (b){ //如果存在
                            return new ServiceStatusInfo<>(0,"充值金币成功",responseMsg);
                        }else{
                            BuyCoinConfigModel coinConfigModel = this.userAssetServiceImpl.findCoinConfigByProductId(product_id,"IOS");
                            UserCoinDetailAddInput addInput = new UserCoinDetailAddInput();
                            addInput.setTitle(coinConfigModel.getTitle());
                            addInput.setNum(coinConfigModel.getCoins());
                            addInput.setExtraData(r_receipt);
                            addInput.setType("PAY");
                            addInput.setTradeNo(transaction_id);
                            addInput.setTradeType("APPLEPAY");
                            addInput.setStatus("SUCCESS");
                            id = this.userAssetServiceImpl.addUserCoinDetailOnce(userId,addInput);
                        }


                    }
                    if (id!=0)a=1;
                    if(a!=0){//用户金币数量新增成功
                        return new ServiceStatusInfo<>(0,"充值金币成功",responseMsg);
                    }else{
                        return new ServiceStatusInfo<>(1,"充值金币失败",responseMsg);
                    }
/************************************************+自己的业务逻辑end**********************************************************/

                } else {
                    return new ServiceStatusInfo<>(1,"receipt数据有问题",responseMsg);
                }
            }

        }catch (Exception e){
            logger.info(e.getMessage());
            return new ServiceStatusInfo<>(1,"出现异常"+e.getMessage(),null);
        }
        return null;

    }


}
