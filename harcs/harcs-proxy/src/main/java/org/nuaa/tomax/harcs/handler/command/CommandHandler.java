package org.nuaa.tomax.harcs.handler.command;

import io.netty.handler.codec.redis.ErrorRedisMessage;
import io.netty.handler.codec.redis.IntegerRedisMessage;
import io.netty.handler.codec.redis.RedisMessage;
import io.netty.handler.codec.redis.SimpleStringRedisMessage;
import org.nuaa.tomax.harcs.common.CommandContext;
import org.nuaa.tomax.harcs.common.ConstantMessage;
import org.nuaa.tomax.harcs.common.LogFactory;
import org.nuaa.tomax.harcs.proxy.IRedisProxyFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

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

        put(ConstantMessage.RC_DBSIZE, ((commands, ctx) ->
                new IntegerRedisMessage(
                        IRedisProxyFactory.get().getMasterNode().dbSize()
                ))
        );

        put(ConstantMessage.RC_FLUSHDB, ((commands, ctx) ->
                new SimpleStringRedisMessage(
                        IRedisProxyFactory.get().getMasterNode().flushDB()
                ))
        );

        put(ConstantMessage.RC_FLUSHALL, ((commands, ctx) ->
                new SimpleStringRedisMessage(
                        IRedisProxyFactory.get().getMasterNode().flushAll()))
        );

        put(ConstantMessage.RC_INFO, ((commands, ctx) ->
                // TODO : commonds size > 3 : err
                new SimpleStringRedisMessage(
                        commands.size() == 1 ?
                                IRedisProxyFactory.get()
                                        .getMasterNode()
                                        .info() :
                                IRedisProxyFactory.get()
                                        .getMasterNode()
                                        .info(commands.get(1))
        )));

        put(ConstantMessage.RC_SET, ((commands, ctx) -> {
            Jedis jedis = IRedisProxyFactory.get().getMasterNode();
            if (commands.size() == 2) {
                return new ErrorRedisMessage(
                        "ERR wrong number of arguments for 'set' command"
                );
            }

            // no mode set
            if (commands.size() == 3) {
                return new SimpleStringRedisMessage(jedis.set(commands.get(1), commands.get(2)));
            }

            switch (commands.get(3)) {
                case ConstantMessage.SP_EX: {
                    if (commands.size() == 4) {
                        return new SimpleStringRedisMessage("ERR syntax error");
                    }

                    if (commands.size() > 6) {
                        return new SimpleStringRedisMessage("ERR syntax error");
                    }

                    int expiredTime = Integer.parseInt(commands.get(4));

                    if (commands.size() == 5) {
                        return new SimpleStringRedisMessage(
                                jedis.set(commands.get(1),
                                        commands.get(2),
                                        new SetParams().ex(expiredTime))
                        );
                    }

                    switch (commands.get(5)) {
                        case ConstantMessage.SP_NX: {
                            return new SimpleStringRedisMessage(
                                    jedis.set(commands.get(1),
                                            commands.get(2),
                                            new SetParams().ex(expiredTime).nx())
                            );
                        }

                        case ConstantMessage.SP_XX: {
                            return new SimpleStringRedisMessage(
                                    jedis.set(commands.get(1),
                                            commands.get(2),
                                            new SetParams().ex(expiredTime).xx())
                            );
                        }

                        default: {
                            return new ErrorRedisMessage(
                                    "ERR wrong number of arguments for 'set' command"
                            );
                        }
                    }
                }

                case ConstantMessage.SP_PX: {
                    if (commands.size() == 4) {
                        return new SimpleStringRedisMessage("ERR syntax error");
                    }

                    if (commands.size() > 6) {
                        return new SimpleStringRedisMessage("ERR syntax error");
                    }

                    long expiredTime = Long.parseLong(commands.get(4));

                    if (commands.size() == 5) {
                        return new SimpleStringRedisMessage(
                                jedis.set(commands.get(1),
                                        commands.get(2),
                                        new SetParams().px(expiredTime))
                        );
                    }

                    switch (commands.get(5)) {
                        case ConstantMessage.SP_NX: {
                            return new SimpleStringRedisMessage(
                                    jedis.set(commands.get(1),
                                            commands.get(2),
                                            new SetParams().px(expiredTime).nx())
                            );
                        }

                        case ConstantMessage.SP_XX: {
                            return new SimpleStringRedisMessage(
                                    jedis.set(commands.get(1),
                                            commands.get(2),
                                            new SetParams().px(expiredTime).xx())
                            );
                        }

                        default: {
                            return new ErrorRedisMessage(
                                    "ERR wrong number of arguments for 'set' command"
                            );
                        }
                    }
                }

                case ConstantMessage.SP_NX: {
                    if (commands.size() > 4) {
                        return new SimpleStringRedisMessage("ERR syntax error");
                    }

                    return new SimpleStringRedisMessage(
                            jedis.set(commands.get(1),
                                    commands.get(2),
                                    new SetParams().nx())
                    );
                }

                case ConstantMessage.SP_XX: {
                    if (commands.size() > 4) {
                        return new SimpleStringRedisMessage("ERR syntax error");
                    }

                    return new SimpleStringRedisMessage(
                            jedis.set(commands.get(1),
                                    commands.get(2),
                                    new SetParams().xx())
                    );
                }

                default: {
                    return new ErrorRedisMessage(
                            "ERR wrong number of arguments for 'set' command"
                    );
                }
            }
        }));

        put(ConstantMessage.RC_GET, ((commands, ctx) -> {
            if (commands.size() == 1 || commands.size() > 2) {
                return new ErrorRedisMessage(
                        "ERR wrong number of arguments for 'get' command"
                );
            }
            String msg = IRedisProxyFactory.get().chooseWorkNode(commands.get(1)).get(commands.get(1));

            if (msg == null) {
                msg = ConstantMessage.RP_NIL;
            }
//            LogFactory.getLog().info("get {} is {}", commands.get(1), msg);
            return new SimpleStringRedisMessage(msg);
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
