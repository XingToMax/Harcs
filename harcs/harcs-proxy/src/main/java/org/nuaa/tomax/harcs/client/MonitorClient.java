package org.nuaa.tomax.harcs.client;

import org.nuaa.tomax.harcs.common.LogFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * @Name: MonitorClient
 * @Description: TODO
 * @Author: tomax
 * @Date: 2019-07-14 14:22
 * @Version: 1.0
 */
public class MonitorClient {
    public static void info(int port) {
        Socket socket = null;
        try {
            socket = new Socket("127.0.0.1", port);

            PrintWriter writer = new PrintWriter(socket.getOutputStream());

            Scanner scanner = new Scanner(socket.getInputStream());

            writer.println("monitor");
            writer.println("info");
            writer.flush();

            StringBuilder builder = new StringBuilder();

            while (scanner.hasNext()) {
                builder.append(scanner.nextLine());
            }

            LogFactory.getLog().info("get info {} from monitor", builder.toString());
        } catch (IOException e) {
            e.printStackTrace();
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
}
