package org.nuaa.tomax.harcs.proxy;

import org.nuaa.tomax.harcs.algorithm.QuerySharding;
import org.nuaa.tomax.harcs.bean.RedisNode;
import org.nuaa.tomax.harcs.envirnoment.RedisSystem;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Name: CoreRedisProxyFactory
 * @Description: TODO
 * @Author: tomax
 * @Date: 2019-07-13 23:16
 * @Version: 1.0
 */
public class CoreRedisProxyFactory implements IRedisProxyFactory {
    private volatile JedisPool master = null;
    private volatile Map<RedisNode, JedisPool> nodesMap = null;

    private volatile static CoreRedisProxyFactory instance = null;

    private Semaphore masterLock;
    private Semaphore nodesMapLock;

    private static KeyHashSharding keyHashSharding = new KeyHashSharding();
    private static LoopSharding loopSharding = new LoopSharding();

    private CoreRedisProxyFactory() {
        // init master and host
        master = new JedisPool(RedisSystem.master.getHost(), RedisSystem.master.getPort());
        nodesMap = new ConcurrentHashMap<>(RedisSystem.MAX_NODES_CONNECTION);

        RedisSystem.aliveNodes.forEach(node -> {
            nodesMap.put(node,
                    RedisSystem.master.equals(node) ?
                    master :
                    new JedisPool(
                            node.getHost(), node.getPort()
                    ));
        });

        masterLock = new Semaphore(1);
        nodesMapLock = new Semaphore(1);
    }

    public static CoreRedisProxyFactory getInstance() {
        if (instance == null) {
            synchronized (CoreRedisProxyFactory.class) {
                if (instance == null) {
                    instance = new CoreRedisProxyFactory();
                }
            }
        }
        return instance;
    }

    @Override
    public Jedis getMasterNode() {
        // wait when master updated
        while (masterLock.availablePermits() != 1){}
        return master.getResource();
    }

    @Override
    public Jedis chooseWorkNode(String key) {
        // wait when nodesMap updated
        // TODO : master change also should wait
        while (nodesMapLock.availablePermits() != 1) {}

        return nodesMap.get(keyHashSharding.sharding(key)).getResource();
    }

    @Override
    public void modifyMaster() {
        try {
            masterLock.acquire();
            master.close();
            master = new JedisPool(RedisSystem.master.getHost(), RedisSystem.master.getPort());
            nodesMap.replace(RedisSystem.master, master);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            masterLock.release();
        }
    }

    @Override
    public void updateWorkNode() {
        try {
            nodesMapLock.acquire();
            nodesMap.clear();
            RedisSystem.aliveNodes.forEach(node -> {
                nodesMap.put(node,
                        RedisSystem.master.equals(node) ?
                                master :
                                new JedisPool(
                                        node.getHost(), node.getPort()
                                ));
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            nodesMapLock.release();
        }
    }

    private final static class LoopSharding implements QuerySharding {
        private AtomicInteger count = new AtomicInteger(0);

        @Override
        public RedisNode sharding(String key) {
            // TODO : visit RedisSystem need concurrency control
            int num = count.incrementAndGet();
            return RedisSystem.aliveNodes.get(num % RedisSystem.aliveNodes.size());
        }
    }

    private final static class KeyHashSharding implements QuerySharding {
        @Override
        public RedisNode sharding(String key) {
            List<RedisNode> nodes = RedisSystem.aliveNodes;

            return nodes.get(key.hashCode() % nodes.size());
        }
    }
}
