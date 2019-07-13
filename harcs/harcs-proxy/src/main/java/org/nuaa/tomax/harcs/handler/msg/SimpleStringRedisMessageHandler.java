package org.nuaa.tomax.harcs.handler.msg;

import org.nuaa.tomax.harcs.bean.RedisMessageHandleContext;
import io.netty.handler.codec.redis.RedisMessage;

/**
 * @Name: SimpleStringRedisMessageHandler
 * @Description: TODO
 * @Author: tomax
 * @Date: 2019-07-13 16:15
 * @Version: 1.0
 */
public class SimpleStringRedisMessageHandler implements RedisMessageHandler {
    private volatile static SimpleStringRedisMessageHandler instance = null;
    private SimpleStringRedisMessageHandler() {}

    public static SimpleStringRedisMessageHandler getInstance() {
        if (instance == null) {
            synchronized (SimpleStringRedisMessageHandler.class) {
                if (instance == null) {
                    instance = new SimpleStringRedisMessageHandler();
                }
            }
        }

        return instance;
    }
    @Override
    public RedisMessage handle(RedisMessage msg, RedisMessageHandleContext context) {
        return null;
    }
}
