package org.nuaa.tomax.harcs.handler.msg;

import org.nuaa.tomax.harcs.bean.RedisMessageHandleContext;
import io.netty.handler.codec.redis.*;

/**
 * @Name: RedisMessageHandler
 * @Description: TODO
 * @Author: tomax
 * @Date: 2019-07-13 16:13
 * @Version: 1.0
 */
public interface RedisMessageHandler {
    RedisMessage handle(RedisMessage msg, RedisMessageHandleContext context);

    static RedisMessage dispatchHandle(RedisMessage msg, RedisMessageHandleContext context) {
        if (msg instanceof ArrayRedisMessage) {
            return ArrayRedisMessageHandler.getInstance().handle(msg, context);
        } else if (msg instanceof ErrorRedisMessage) {
            return ErrorRedisMessageHandler.getInstance().handle(msg, context);
        } else if (msg instanceof FullBulkStringRedisMessage) {
            return FullBulkStringRedisMessageHandler.getInstance().handle(msg, context);
        } else if (msg instanceof IntegerRedisMessage) {
            return IntegerRedisMessageHandler.getInstance().handle(msg, context);
        } else if (msg instanceof SimpleStringRedisMessage) {
            return SimpleStringRedisMessageHandler.getInstance().handle(msg, context);
        } else {
            return new ErrorRedisMessage("error message type");
        }
    }
}
