package org.nuaa.tomax.harcs.bean;

import lombok.Data;

/**
 * @Name: org.nuaa.tomax.harcs.bean.ProxyContext
 * @Description: TODO
 * @Author: tomax
 * @Date: 2019-07-01 20:43
 * @Version: 1.0
 */
@Data
public class ProxyContext {
    private int port;

    public ProxyContext(int port) {
        this.port = port;
    }
}
