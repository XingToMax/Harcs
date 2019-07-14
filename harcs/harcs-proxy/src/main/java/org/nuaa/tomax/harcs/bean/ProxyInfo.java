package org.nuaa.tomax.harcs.bean;

import org.nuaa.tomax.harcs.envirnoment.RedisSystem;

/**
 * @Name: ProxyInfo
 * @Description: TODO
 * @Author: tomax
 * @Date: 2019-07-13 21:28
 * @Version: 1.0
 */
public class ProxyInfo {

    public volatile static String proxyHost;
    public volatile static int proxyPort;

    public volatile static String proxyRole;

    public static void init() {

    }
}
