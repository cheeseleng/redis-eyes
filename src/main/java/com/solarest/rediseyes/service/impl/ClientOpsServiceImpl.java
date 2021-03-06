package com.solarest.rediseyes.service.impl;

import com.solarest.rediseyes.client.RedisClient;
import com.solarest.rediseyes.client.SingletonContainer;
import com.solarest.rediseyes.exception.NonClientException;
import com.solarest.rediseyes.service.ClientOpsService;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;

import java.util.*;

/**
 * Created by JinJian on 17-8-2.
 */
@Service
public class ClientOpsServiceImpl implements ClientOpsService {

    @Override
    public List<String> scanKeys(String conn, String pattern, Integer cursor, Integer size) throws NonClientException {
        Jedis jedis = null;
        List<String> keys;
        RedisClient client = SingletonContainer.getSingleton().getRedisClient(conn);
        try {
            jedis = client.getResource();
            if ("*".equals(pattern)) {
                ScanParams scanParams = new ScanParams().match(pattern).count(size);
                keys = jedis.scan(String.valueOf(cursor), scanParams).getResult();
            } else {
                ScanParams scanParams = new ScanParams().match(pattern).count(Math.toIntExact(jedis.dbSize()));
                keys = jedis.scan("0", scanParams).getResult();
            }
        } finally {
            client.releaseResource(jedis);
        }
        return keys;
    }

    @Override
    public Integer countKeys(String conn) throws NonClientException {
        Jedis jedis = null;
        Integer count;
        RedisClient client = SingletonContainer.getSingleton().getRedisClient(conn);
        try {
            jedis = client.getResource();
            count = Math.toIntExact(jedis.dbSize());
        } finally {
            client.releaseResource(jedis);
        }
        return count;
    }

    @Override
    public String keyType(String conn, String k) throws NonClientException {
        String keyType;
        Jedis jedis = null;
        RedisClient client = SingletonContainer.getSingleton().getRedisClient(conn);
        try {
            jedis = client.getResource();
            keyType = jedis.type(k);
        } finally {
            client.releaseResource(jedis);
        }
        return keyType;
    }

    @Override
    public void removeKeys(String conn, String... k) throws NonClientException {
        Jedis jedis = null;
        RedisClient client = SingletonContainer.getSingleton().getRedisClient(conn);
        try {
            jedis = client.getResource();
            jedis.del(k);
        } finally {
            client.releaseResource(jedis);
        }
    }
}
