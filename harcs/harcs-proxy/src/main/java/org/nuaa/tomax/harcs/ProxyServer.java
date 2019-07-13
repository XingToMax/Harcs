package org.nuaa.tomax.harcs;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.redis.RedisArrayAggregator;
import io.netty.handler.codec.redis.RedisBulkStringAggregator;
import io.netty.handler.codec.redis.RedisDecoder;
import io.netty.handler.codec.redis.RedisEncoder;
import lombok.extern.java.Log;
import org.nuaa.tomax.harcs.bean.ProxyContext;
import org.nuaa.tomax.harcs.handler.RedisCommandHandler;

import java.util.logging.Level;

/**
 * @Name: org.nuaa.tomax.harcs.ProxyServer
 * @Description: TODO
 * @Author: tomax
 * @Date: 2019-07-01 20:42
 * @Version: 1.0
 */
@Log
public class ProxyServer {
    private ProxyContext context;

    public ProxyServer(ProxyContext context) {
        this.context = context;
    }

    public void start() {
        NioEventLoopGroup boss = new NioEventLoopGroup(2);
        NioEventLoopGroup work = new NioEventLoopGroup(4);

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss, work)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new RedisDecoder());
                            socketChannel.pipeline().addLast(new RedisBulkStringAggregator());
                            socketChannel.pipeline().addLast(new RedisArrayAggregator());
                            socketChannel.pipeline().addLast(new RedisEncoder());
                            socketChannel.pipeline().addLast(new RedisCommandHandler());
                        }
                    });

            ChannelFuture future = bootstrap.bind(context.getPort()).sync();

            log.info("redis proxy server start to listen on port : " + context.getPort());

            future.channel().closeFuture().sync();
        } catch (Exception e) {
            log.log(Level.WARNING, e.getMessage(), e.getCause());
        } finally {
            try {
                boss.shutdownGracefully().sync();
                work.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void shutdown() {

    }

    public static void main(String[] args) {
        new ProxyServer(new ProxyContext(6380)).start();
    }
}
