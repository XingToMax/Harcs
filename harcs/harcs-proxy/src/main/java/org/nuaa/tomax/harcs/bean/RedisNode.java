package org.nuaa.tomax.harcs.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Name: RedisNode
 * @Description: TODO
 * @Author: tomax
 * @Date: 2019-07-13 19:19
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RedisNode {
    private String host;
    private int port;

    private String role;

    public RedisNode(String host, int port) {
        this.host = host;
        this.port = port;
    }
}
