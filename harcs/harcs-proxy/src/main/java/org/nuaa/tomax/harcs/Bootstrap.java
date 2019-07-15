package org.nuaa.tomax.harcs;

import org.nuaa.tomax.harcs.bean.ProxyContext;
import org.nuaa.tomax.harcs.bean.RedisNode;
import org.nuaa.tomax.harcs.client.MonitorClient;
import org.nuaa.tomax.harcs.common.ConfigUtil;
import org.nuaa.tomax.harcs.common.LogFactory;
import org.nuaa.tomax.harcs.common.NetworkUtil;
import org.nuaa.tomax.harcs.envirnoment.RedisSystem;

import java.util.Properties;

/**
 * @Name: Bootstrap
 * @Description: TODO
 * @Author: tomax
 * @Date: 2019-07-14 14:29
 * @Version: 1.0
 */
public class Bootstrap {
    public static void main(String[] args) {
        Properties properties = ConfigUtil.loadProperties("conf.properties");

        ProxyContext context = new ProxyContext(ConfigUtil.getInt(properties, "proxy.port"));

        ProxyContext.monitorPort = ConfigUtil.getInt(properties, "monitor.port");

        RedisSystem.master = new RedisNode(ConfigUtil.getString(properties, "redis.master.host"), ConfigUtil.getInt(properties, "redis.master.port"));
        RedisSystem.aliveNodes.add(RedisSystem.master);
        RedisNode slave1 = new RedisNode(ConfigUtil.getString(properties, "redis.slave1.host"), ConfigUtil.getInt(properties, "redis.slave1.port"));
        RedisNode slave2 = new RedisNode(ConfigUtil.getString(properties, "redis.slave2.host"), ConfigUtil.getInt(properties, "redis.slave2.port"));
        RedisNode slave3 = new RedisNode(ConfigUtil.getString(properties, "redis.slave3.host"), ConfigUtil.getInt(properties, "redis.slave3.port"));
        RedisNode slave4 = new RedisNode(ConfigUtil.getString(properties, "redis.slave4.host"), ConfigUtil.getInt(properties, "redis.slave4.port"));
        RedisNode slave5 = new RedisNode(ConfigUtil.getString(properties, "redis.slave5.host"), ConfigUtil.getInt(properties, "redis.slave5.port"));

        if (NetworkUtil.ping(slave1.getHost(), slave1.getPort())) {
            RedisSystem.aliveNodes.add(slave1);
            LogFactory.getLog().info("slave1 alive");
        }

        if (NetworkUtil.ping(slave2.getHost(), slave2.getPort())) {
            RedisSystem.aliveNodes.add(slave2);
            LogFactory.getLog().info("slave2 alive");
        }

        if (NetworkUtil.ping(slave3.getHost(), slave3.getPort())) {
            RedisSystem.aliveNodes.add(slave3);
            LogFactory.getLog().info("slave3 alive");
        }

        if (NetworkUtil.ping(slave4.getHost(), slave4.getPort())) {
            RedisSystem.aliveNodes.add(slave4);
            LogFactory.getLog().info("slave4 alive");
        }

        if (NetworkUtil.ping(slave5.getHost(), slave5.getPort())) {
            RedisSystem.aliveNodes.add(slave5);
            LogFactory.getLog().info("slave5 alive");
        }

        ProxyServer server = new ProxyServer(context);
        server.start();
    }
}
