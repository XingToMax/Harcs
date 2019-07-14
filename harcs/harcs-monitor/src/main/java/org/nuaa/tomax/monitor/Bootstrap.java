package org.nuaa.tomax.monitor;

/**
 * @Name: Bootstrap
 * @Description: TODO
 * @Author: tomax
 * @Date: 2019-07-14 11:21
 * @Version: 1.0
 */
public class Bootstrap {
    public static void main(String[] args) {
        MonitorServer server = new MonitorServer();

        server.start(8087);
    }
}
