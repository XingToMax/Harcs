package org.nuaa.tomax.harcs.bean;

import lombok.Data;
import org.nuaa.tomax.harcs.envirnoment.RedisSystem;

/**
 * @Name: org.nuaa.tomax.harcs.bean.ProxyContext
 * @Description: TODO
 * @Author: tomax
 * @Date: 2019-07-01 20:43
 * @Version: 1.0
 */
@Data
public class ProxyContext {
    private RedisSystem redisSystem;
    private ProxyInfo proxyInfo;

    private int port;

    public static int monitorPort;

    public ProxyContext(int port) {
        this.port = port;
    }
}
