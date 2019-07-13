package org.nuaa.tomax.harcs.common;

import io.netty.handler.codec.redis.*;
import io.netty.util.CharsetUtil;
import lombok.extern.java.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * @Name: RedisMessageUtil
 * @Description: TODO
 * @Author: tomax
 * @Date: 2019-07-13 16:33
 * @Version: 1.0
 */
@Log
public class RedisMessageUtil {
    public static void printRedisMessage(RedisMessage msg) {
        if (msg instanceof ArrayRedisMessage) {
            for (RedisMessage rm : ((ArrayRedisMessage) msg).children()) {
                printRedisMessage(rm);
            }
        } else if (msg instanceof ErrorRedisMessage) {
            log.info("redis error msg : " + ((ErrorRedisMessage) msg).content());
        } else if (msg instanceof FullBulkStringRedisMessage) {
            log.info("redis full bulk string msg : " + extractStringFromFullBulkString((FullBulkStringRedisMessage) msg));
        } else if (msg instanceof IntegerRedisMessage) {
            log.info("redis int msg : " + ((IntegerRedisMessage) msg).value());
        } else if (msg instanceof SimpleStringRedisMessage) {
            log.info("redis simple string msg : " + ((SimpleStringRedisMessage) msg).content());
        } else {
            log.info("error redis type");
        }
    }

    public static String extractStringFromFullBulkString(FullBulkStringRedisMessage msg) {
        return !msg.isNull() ? msg.content().toString(CharsetUtil.UTF_8) :
                ConstantMessage.RP_NIL;
    }

    public static String commandExetract(String command) {
        return command.toLowerCase();
    }

    public static List<String> extractStringListFromArrayMessage(ArrayRedisMessage msg) {
        List<String> list = new ArrayList<>(msg.children().size());
        for (RedisMessage redisMessage : msg.children()) {
            if (redisMessage instanceof SimpleStringRedisMessage) {
                list.add(((SimpleStringRedisMessage) redisMessage).content());
            } else if (redisMessage instanceof FullBulkStringRedisMessage) {
                list.add(extractStringFromFullBulkString((FullBulkStringRedisMessage) redisMessage));
            } else if (redisMessage instanceof IntegerRedisMessage) {
                list.add(String.valueOf(((IntegerRedisMessage) redisMessage).value()));
            }
        }

        return list;
    }


}
