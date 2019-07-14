package org.nuaa.tomax.harcs.service;

import org.nuaa.tomax.harcs.common.NetworkUtil;
import org.nuaa.tomax.harcs.envirnoment.RedisSystem;

import java.util.TimerTask;

/**
 * @Name: RedisNodeScheduleService
 * @Description: TODO
 * @Author: tomax
 * @Date: 2019-07-14 09:49
 * @Version: 1.0
 */
public class RedisNodeScheduleService extends TimerTask {
    @Override
    public void run() {
        heartbeat();
    }

    public static void heartbeat() {
        synchronized (RedisSystem.ALIVE_NODES_LOCK) {
            RedisSystem.aliveNodes.removeIf(node -> {
                if (!NetworkUtil.ping(node.getHost(), node.getPort())){
                    RedisSystem.deadNodes.add(node);
                    return true;
                }
                return false;
            });

            RedisSystem.deadNodes.removeIf(node -> {
               if (NetworkUtil.ping(node.getHost(), node.getPort())) {
                   RedisSystem.aliveNodes.add(node);
                   return true;
               }

               return false;
            });

            if (!NetworkUtil.ping(RedisSystem.master.getHost(), RedisSystem.master.getPort())) {
                // TODO : select new master node

            }
        }
    }

    public static void chooseMaster() {

    }
}
