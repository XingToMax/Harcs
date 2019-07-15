package org.nuaa.tomax.harcs.handler.msg;

import io.netty.handler.codec.redis.ArrayRedisMessage;
import io.netty.handler.codec.redis.ErrorRedisMessage;
import io.netty.handler.codec.redis.SimpleStringRedisMessage;
import org.nuaa.tomax.harcs.bean.RedisMessageHandleContext;
import io.netty.handler.codec.redis.RedisMessage;
import org.nuaa.tomax.harcs.common.CommandContext;
import org.nuaa.tomax.harcs.common.ConstantMessage;
import org.nuaa.tomax.harcs.common.LogFactory;
import org.nuaa.tomax.harcs.common.RedisMessageUtil;
import org.nuaa.tomax.harcs.handler.command.CommandHandler;

import java.util.List;

/**
 * @Name: SimpleStringRedisMessageHandler
 * @Description: TODO
 * @Author: tomax
 * @Date: 2019-07-13 16:15
 * @Version: 1.0
 */
public class ArrayRedisMessageHandler implements RedisMessageHandler {
    private volatile static ArrayRedisMessageHandler instance = null;
    private ArrayRedisMessageHandler() {}

    public static ArrayRedisMessageHandler getInstance() {
        if (instance == null) {
            synchronized (ArrayRedisMessageHandler.class) {
                if (instance == null) {
                    instance = new ArrayRedisMessageHandler();
                }
            }
        }

        return instance;
    }

    @Override
    public RedisMessage handle(RedisMessage msg, RedisMessageHandleContext context) {
        ArrayRedisMessage arrayRedisMessage = (ArrayRedisMessage) msg;

        List<String> commandsList = RedisMessageUtil.extractStringListFromArrayMessage(arrayRedisMessage);

        if (commandsList.size() == 0) {
            return new ErrorRedisMessage(ConstantMessage.RP_NIL);
        }
        String command = commandsList.get(0).toLowerCase();
        if (!CommandHandler.commandHandleMap.containsKey(command)) {
            return new ErrorRedisMessage("ERR unknown command `" + commandsList.get(0) + "` with args beginning with:");
        }

        return CommandHandler.commandHandleMap.get(command).handle(commandsList, new CommandContext());
    }
}
