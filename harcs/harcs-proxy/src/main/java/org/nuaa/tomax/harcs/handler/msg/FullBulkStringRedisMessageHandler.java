package org.nuaa.tomax.harcs.handler.msg;

import io.netty.handler.codec.redis.SimpleStringRedisMessage;
import org.nuaa.tomax.harcs.bean.RedisMessageHandleContext;
import io.netty.handler.codec.redis.RedisMessage;
import org.nuaa.tomax.harcs.common.ConstantMessage;

/**
 * @Name: SimpleStringRedisMessageHandler
 * @Description: TODO
 * @Author: tomax
 * @Date: 2019-07-13 16:15
 * @Version: 1.0
 */
public class FullBulkStringRedisMessageHandler implements RedisMessageHandler {
    private volatile static FullBulkStringRedisMessageHandler instance = null;
    private FullBulkStringRedisMessageHandler() {}

    public static FullBulkStringRedisMessageHandler getInstance() {
        if (instance == null) {
            synchronized (FullBulkStringRedisMessageHandler.class) {
                if (instance == null) {
                    instance = new FullBulkStringRedisMessageHandler();
                }
            }
        }

        return instance;
    }

    @Override
    public RedisMessage handle(RedisMessage msg, RedisMessageHandleContext context) {
        return new SimpleStringRedisMessage(ConstantMessage.RP_OK);
    }
}
