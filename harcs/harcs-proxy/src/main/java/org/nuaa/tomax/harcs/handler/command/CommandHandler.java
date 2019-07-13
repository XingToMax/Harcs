package org.nuaa.tomax.harcs.handler.command;

import io.netty.handler.codec.redis.IntegerRedisMessage;
import io.netty.handler.codec.redis.RedisMessage;
import io.netty.handler.codec.redis.SimpleStringRedisMessage;
import org.nuaa.tomax.harcs.common.CommandContext;
import org.nuaa.tomax.harcs.common.ConstantMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Name: CommandHandler
 * @Description: TODO
 * @Author: tomax
 * @Date: 2019-07-13 17:07
 * @Version: 1.0
 */
public interface CommandHandler {
    RedisMessage handle(List<String> commands, CommandContext ctx);

    public static Map<String, CommandHandler> commandHandleMap = new HashMap<String, CommandHandler>() {{

        put(ConstantMessage.RC_COMMAND, ((commands, ctx) -> {
            return new SimpleStringRedisMessage(ConstantMessage.RP_OK);
        }));

        put(ConstantMessage.RC_PING, ((commands, ctx) -> {
            return new SimpleStringRedisMessage(ConstantMessage.RP_PONG);
        }));

        put(ConstantMessage.RC_AUTH, (commands, ctx) -> {
            return new SimpleStringRedisMessage(ConstantMessage.RP_OK);
        });

        put(ConstantMessage.RC_DBSIZE, ((commands, ctx) -> {
            return new IntegerRedisMessage(10);
        }));

        put(ConstantMessage.RC_FLUSHDB, ((commands, ctx) -> {
            return new SimpleStringRedisMessage(ConstantMessage.RP_OK);
        }));

        put(ConstantMessage.RC_FLUSHALL, ((commands, ctx) -> {
            return new SimpleStringRedisMessage(ConstantMessage.RP_OK);
        }));

        put(ConstantMessage.RC_INFO, ((commands, ctx) -> {
            return new SimpleStringRedisMessage("info ... ");
        }));

        put(ConstantMessage.RC_SET, ((commands, ctx) -> {
            return new SimpleStringRedisMessage(ConstantMessage.RP_OK);
        }));

        put(ConstantMessage.RC_GET, ((commands, ctx) -> {
            return new SimpleStringRedisMessage("world");
        }));

        put(ConstantMessage.RC_SELECT, ((commands, ctx) -> {
            return new SimpleStringRedisMessage(ConstantMessage.RP_OK);
        }));

        put(ConstantMessage.RC_ECHO, ((commands, ctx) -> {
            StringBuilder builder = new StringBuilder();
            for (int i = 1; i < commands.size(); i++) {
                builder.append(commands);
                builder.append(" ");
            }
            return new SimpleStringRedisMessage(builder.toString());
        }));


        // TODO : handle other commands
    }};
}
