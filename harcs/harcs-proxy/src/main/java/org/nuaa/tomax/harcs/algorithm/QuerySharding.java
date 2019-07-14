package org.nuaa.tomax.harcs.algorithm;

import org.nuaa.tomax.harcs.bean.RedisNode;

/**
 * @Name: QuerySharding
 * @Description: TODO
 * @Author: tomax
 * @Date: 2019-07-13 19:17
 * @Version: 1.0
 */
public interface QuerySharding {
    RedisNode sharding(String key);
}
