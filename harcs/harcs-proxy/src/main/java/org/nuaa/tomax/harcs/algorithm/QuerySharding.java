package org.nuaa.tomax.harcs.algorithm;

import redis.clients.jedis.Jedis;

/**
 * @Name: QuerySharding
 * @Description: TODO
 * @Author: tomax
 * @Date: 2019-07-13 19:17
 * @Version: 1.0
 */
public interface QuerySharding {
    Jedis sharding(String key);
}
