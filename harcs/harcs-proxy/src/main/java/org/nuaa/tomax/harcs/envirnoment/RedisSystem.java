package org.nuaa.tomax.harcs.envirnoment;

import org.nuaa.tomax.harcs.bean.RedisNode;

import java.util.LinkedList;
import java.util.List;

/**
 * @Name: RedisSystem
 * @Description: TODO
 * @Author: tomax
 * @Date: 2019-07-13 19:19
 * @Version: 1.0
 */
public class RedisSystem {
    public static final int MAX_NODES_CONNECTION = 10;
    public volatile static RedisNode master = null;

    public volatile static List<RedisNode> aliveNodes = null;

    public volatile static List<RedisNode> deadNodes = null;

    public static final Object ALIVE_NODES_LOCK = new Object();

    public static final Object MASTER_LOCK = new Object();

    public static final Object DEAD_NODES_LOCK = new Object();

    static {
        master = new RedisNode("127.0.0.1", 6379);
        aliveNodes = new LinkedList<>();

        aliveNodes.add(master);

        deadNodes = new LinkedList<>();
    }
}
