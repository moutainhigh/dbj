package com.zwdbj.server.common.mq;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MQConfig {
    /**
     * 比如耗时的操作队列，比如发送验证码等操作
     */
    public static final String queueTimeConsuming = "queue_timeConsuming";
    public static final String delayedQueueTimeConsuming = "delayedQueueTimeConsuming";
    public static final String delayedExchangeTimeMachine = "delayedExchangeTimeMachine";
    public static final String delayedROUTING_KEY = "delayedROUTING_KEY";
    /**
     * 数据状态交换机
     */
    public static final String exchangeTimeMachine = "exchange_timeMachine";
}
