package org.nuaa.tomax.harcs.proxy;

import org.nuaa.tomax.harcs.algorithm.QuerySharding;
import org.nuaa.tomax.harcs.bean.RedisNode;
import org.nuaa.tomax.harcs.client.RedisClient;
import org.nuaa.tomax.harcs.common.LogFactory;
import org.nuaa.tomax.harcs.envirnoment.RedisSystem;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
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
    private volatile Jedis master = null;
    private volatile Map<RedisNode, Jedis> nodesMap = null;
    private volatile RedisClient masterClient = null;
    private volatile Map<RedisNode, RedisClient> clientMap = null;

    private volatile static CoreRedisProxyFactory instance = null;

    private Semaphore masterLock;
    private Semaphore nodesMapLock;

    private static KeyHashSharding keyHashSharding = new KeyHashSharding();
    private static LoopSharding loopSharding = new LoopSharding();
    private static RandomHashSharding randomHashSharding = new RandomHashSharding();

    private CoreRedisProxyFactory() {
        // init master and host
        master = new Jedis(RedisSystem.master.getHost(), RedisSystem.master.getPort());
        nodesMap = new ConcurrentHashMap<>();

        RedisSystem.aliveNodes.forEach(node -> {
            nodesMap.put(node,
                    RedisSystem.master.equals(node) ?
                    master :
                    new Jedis(
                            node.getHost(), node.getPort()
                    ));
        });


        masterClient = new RedisClient(RedisSystem.master.getHost(), RedisSystem.master.getPort());
        clientMap = new ConcurrentHashMap<>();
        RedisSystem.aliveNodes.forEach(node -> {
            clientMap.put(node,
                    RedisSystem.master.equals(node) ?
                    masterClient:
                    new RedisClient(
                            node.getHost(), node.getPort()
                    ));
        });

        clientMap.forEach((k, v) -> {
            try {
                v.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        try {
            masterClient.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

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
//        while (masterLock.availablePermits() != 1){}
        return master;
    }

    @Override
    public Jedis chooseWorkNode(String key) {
        // wait when nodesMap updated
        // TODO : master change also should wait
//        while (nodesMapLock.availablePermits() != 1) {}
        return nodesMap.get(keyHashSharding.sharding(key));
    }

    @Override
    public void modifyMaster() {
        try {
            masterLock.acquire();
            master.close();
            master = new Jedis(RedisSystem.master.getHost(), RedisSystem.master.getPort());
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
                                new Jedis(
                                        node.getHost(), node.getPort()
                                ));
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            nodesMapLock.release();
        }
    }

    @Override
    public RedisClient getMasterClient() {
        return masterClient;
    }

    @Override
    public RedisClient chooseWorkClient(String key) {
        RedisNode node = randomHashSharding.sharding(key);
        return clientMap.get(node);
    }

    private final static class LoopSharding implements QuerySharding {
        private AtomicInteger count = new AtomicInteger(0);
        private AtomicInteger c1 = new AtomicInteger(0);
        private AtomicInteger c2 = new AtomicInteger(0);
        private AtomicInteger c3 = new AtomicInteger(0);
        private AtomicInteger c4 = new AtomicInteger(0);
        private AtomicInteger c5 = new AtomicInteger(0);
        private AtomicInteger c6 = new AtomicInteger(0);

        private AtomicInteger[] list = new AtomicInteger[] {
                c1, c2, c3, c4, c5, c6
        };
        @Override
        public RedisNode sharding(String key) {
            // TODO : visit RedisSystem need concurrency control
            int num = count.incrementAndGet();
            if (num % 10000 == 0) {
                LogFactory.getLog().info("sharding1 - {}", ((float)list[0].get()) / num);
                LogFactory.getLog().info("sharding2 - {}", ((float)list[1].get()) / num);
                LogFactory.getLog().info("sharding3 - {}", ((float)list[2].get()) / num);
                LogFactory.getLog().info("sharding4 - {}", ((float)list[3].get()) / num);
                LogFactory.getLog().info("sharding5 - {}", ((float)list[4].get()) / num);
                LogFactory.getLog().info("sharding6 - {}", ((float)list[5].get()) / num);

            }
            int choose = num % RedisSystem.aliveNodes.size();
            list[choose].incrementAndGet();
            return RedisSystem.aliveNodes.get(choose);
        }
    }

    private final static class KeyHashSharding implements QuerySharding {
        @Override
        public RedisNode sharding(String key) {
            List<RedisNode> nodes = RedisSystem.aliveNodes;
            return nodes.get(key.hashCode() % nodes.size());
        }
    }
    private final static class RandomHashSharding implements QuerySharding {
        final Random random = new Random();
        @Override
        public RedisNode sharding(String key) {
            return RedisSystem.aliveNodes.get(random.nextInt(RedisSystem.aliveNodes.size()));
        }
    }
}
