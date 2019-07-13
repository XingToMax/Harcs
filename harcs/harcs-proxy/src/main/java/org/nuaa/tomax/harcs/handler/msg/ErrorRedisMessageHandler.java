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
public class ErrorRedisMessageHandler implements RedisMessageHandler {
    private volatile static ErrorRedisMessageHandler instance = null;
    private ErrorRedisMessageHandler() {}

    public static ErrorRedisMessageHandler getInstance() {
        if (instance == null) {
            synchronized (ErrorRedisMessageHandler.class) {
                if (instance == null) {
                    instance = new ErrorRedisMessageHandler();
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
