package org.nuaa.tomax.harcs.common;

import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @Name: NetworkUtil
 * @Description: TODO
 * @Author: tomax
 * @Date: 2019-07-14 09:40
 * @Version: 1.0
 */
public class NetworkUtil {
    public static final int TIMEOUT = 500;

    public static boolean ping(String host, int port) {
        Socket socket = null;
        try {
            socket = new Socket(host, port);
            return socket.isConnected();
        } catch (IOException e) {
            return false;
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean isOpen(String host) {
        try {
            return InetAddress.getByName(host).isReachable(TIMEOUT);
        } catch (IOException e) {
            LogFactory.getLog().info("host {} is not open", host);
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println(ping("127.0.0.1", 8080));
    }
}
