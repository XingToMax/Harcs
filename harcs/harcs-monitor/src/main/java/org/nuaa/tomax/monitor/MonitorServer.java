package org.nuaa.tomax.monitor;

import org.nuaa.tomax.monitor.handler.SocketHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Name: MonitorServer
 * @Description: TODO
 * @Author: tomax
 * @Date: 2019-07-14 11:25
 * @Version: 1.0
 */
public class MonitorServer {
    public static final int TIMEOUT = 1000;

    public void start(int port) {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
            while (true) {
                Socket socket = server.accept();
                executor.execute(new SocketHandler(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (server != null) {
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
