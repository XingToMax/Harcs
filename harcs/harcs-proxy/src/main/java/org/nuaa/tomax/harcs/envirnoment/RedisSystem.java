package org.nuaa.tomax.harcs.envirnoment;

import org.nuaa.tomax.harcs.bean.RedisNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @Name: RedisSystem
 * @Description: TODO
 * @Author: tomax
 * @Date: 2019-07-13 19:19
 * @Version: 1.0
 */
public class RedisSystem {
    public volatile static RedisNode master = null;

    public volatile static List<RedisNode> aliveNodes = null;

    static {
        master = new RedisNode("127.0.0.1", 6379);
        aliveNodes = new ArrayList<>();

        aliveNodes.add(master);
    }
}
