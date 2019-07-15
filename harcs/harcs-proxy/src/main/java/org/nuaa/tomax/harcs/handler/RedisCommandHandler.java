package org.nuaa.tomax.harcs.handler;

import io.netty.util.ReferenceCountUtil;
import org.nuaa.tomax.harcs.bean.RedisMessageHandleContext;
import org.nuaa.tomax.harcs.common.RedisMessageUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.redis.*;
import org.nuaa.tomax.harcs.handler.msg.RedisMessageHandler;

/**
 * @Name: RedisCommandHandler
 * @Description: TODO
 * @Author: tomax
 * @Date: 2019-07-02 15:39
 * @Version: 1.0
 */
public class RedisCommandHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RedisMessage redisMessage = (RedisMessage) msg;

//        RedisMessageUtil.printRedisMessage(redisMessage);
        RedisMessage reply = RedisMessageHandler.dispatchHandle(redisMessage, new RedisMessageHandleContext());

        ctx.writeAndFlush(reply);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

    }
}
