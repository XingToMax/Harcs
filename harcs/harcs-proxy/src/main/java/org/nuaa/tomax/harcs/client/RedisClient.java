package org.nuaa.tomax.harcs.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.redis.*;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.GenericFutureListener;
import org.nuaa.tomax.harcs.common.RedisMessageUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.CountDownLatch;

/**
 * @Author: ToMax
 * @Description:
 * @Date: Created in 2019/7/14 17:05
 */
public class RedisClient {

    String host;    //   目标主机
    int port;       //   目标主机端口
    public RedisClient(String host,int port){
        this.host = host;
        this.port = port;
    }
    ChannelFuture lastWriteFuture = null;
    Channel channel = null;

    public void start() throws Exception{
        EventLoopGroup group = new NioEventLoopGroup();
//        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            channel.pipeline().addLast(new RedisDecoder());
                            channel.pipeline().addLast(new RedisBulkStringAggregator());
                            channel.pipeline().addLast(new RedisArrayAggregator());
                            channel.pipeline().addLast(new RedisEncoder());
                            channel.pipeline().addLast(new RedisDecoder());
                            channel.pipeline().addLast(new RedisClientHandler());
                        }
                    });

            channel = bootstrap.connect(host, port).sync().channel();
//            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
//            ChannelFuture lastWriteFuture = null;
//            for (;;) {
//                String s = in.readLine();
//                if(s.equalsIgnoreCase("quit")) {
//                    break;
//                }
//                System.out.print(">");
//                lastWriteFuture = channel.writeAndFlush(s);
//                lastWriteFuture.addListener(new GenericFutureListener<ChannelFuture>() {
//                    @Override
//                    public void operationComplete(ChannelFuture future) throws Exception {
//                        if (!future.isSuccess()) {
//                            System.err.print("write failed: ");
//                            future.cause().printStackTrace(System.err);
//                        }
//                    }
//                });
//            }
//            if (lastWriteFuture != null) {
//                lastWriteFuture.sync();
//            }
//            if (lastWriteFuture != null) {
//                lastWriteFuture.sync();
//            }
//            System.out.println(" bye ");

//        }finally {
//            group.shutdownGracefully();
//        }
    }

    public String get(String key) {
        try {
            channel.writeAndFlush("get " + key).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "";
    }

    public String set(String key, String value) {
        try {
            channel.writeAndFlush("set " + key + " " + value).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return RedisMessageUtil.extractMsg(RedisClientHandler.currentRedisMessageMap.get(channel));
    }

    public static void main(String[] args) throws Exception{
        RedisClient client = new RedisClient("127.0.0.1",8086);
        client.start();
//        client.get("a");
        int threadNum = 10;
        CountDownLatch countDownLatch = new CountDownLatch(threadNum);
        long beg = System.currentTimeMillis();
        for (int i = 1; i <= threadNum; i++) {
            new Thread(() -> {
                for (int j = 1; j <= 2000; j++) {
                    client.get("a");
                    client.get("b");
                    client.get("c");
                    client.get("d");
                    client.get("e");
                }
                countDownLatch.countDown();
            }).start();
        }

        countDownLatch.await();
        System.out.println((float) (System.currentTimeMillis() - beg) / 1000);
    }

}
