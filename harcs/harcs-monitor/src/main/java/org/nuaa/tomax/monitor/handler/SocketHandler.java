package org.nuaa.tomax.monitor.handler;

import org.json.JSONObject;
import org.nuaa.tomax.monitor.bean.SystemData;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * @Name: SocketHandler
 * @Description: TODO
 * @Author: tomax
 * @Date: 2019-07-14 11:31
 * @Version: 1.0
 */
public class SocketHandler extends Thread {
    private Socket socket;

    public SocketHandler(Socket socket) {
        this.socket = socket;
    }

    public static final String COMMAND_HEAD = "monitor";

    @Override
    public void run() {

        try {
            Scanner scanner = new Scanner(socket.getInputStream());
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            String head = scanner.nextLine();

            if (!head.startsWith(COMMAND_HEAD)) {
                writer.println("error head");
                return;
            }
            String command = scanner.nextLine();

            switch (command) {
                case "ping": {
                    writer.println("pong");
                    writer.flush();
                    return;
                }

                case "info": {
                    JSONObject jsonObject = new JSONObject(new SystemData("hello"));
                    writer.println(jsonObject.toString());
                    writer.flush();
                    return;
                }

                case "sync": {
                    writer.println("sync");
                    writer.flush();
                    return;
                }

                default: {
                    writer.println("error(unknown command)");
                    writer.flush();
                }
            }


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
