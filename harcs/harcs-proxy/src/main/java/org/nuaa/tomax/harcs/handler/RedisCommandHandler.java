package org.nuaa.tomax.harcs.handler;

import org.nuaa.tomax.harcs.handler.bean.RedisMessageHandleContext;
import org.nuaa.tomax.harcs.handler.msg.RedisMessageHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.redis.*;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Name: RedisCommandHandler
 * @Description: TODO
 * @Author: tomax
 * @Date: 2019-07-02 15:39
 * @Version: 1.0
 */
public class RedisCommandHandler extends ChannelInboundHandlerAdapter {
    public static ConcurrentHashMap<String, ChannelInfo> channelMessages = new ConcurrentHashMap<String, ChannelInfo>();
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RedisMessage redisMessage = (RedisMessage) msg;

        RedisMessageHandler.dispatchHandle(redisMessage, new RedisMessageHandleContext());

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String id = ctx.channel().id().asShortText();
        ChannelInfo info = new ChannelInfo();
//        if ("".equals(Configuration.HAREDIS_ACCESS_PASSWORD)) {
            info.setAuth(true);
//        }
        channelMessages.put(id, info);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        String id = ctx.channel().id().asShortText();
        channelMessages.remove(id);
        Channel channel = ctx.channel();
        if (channel.isActive()) {
            ctx.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        String id = ctx.channel().id().asShortText();
        channelMessages.remove(id);
        Channel channel = ctx.channel();
        if (channel.isActive()) {
            ctx.close();
        }
    }
}
