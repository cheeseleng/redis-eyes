package com.solarest.rediseyes.service.impl;

import com.solarest.rediseyes.client.SingletonContainer;
import com.solarest.rediseyes.exception.NonClientExcept;
import com.solarest.rediseyes.service.ClientOpsService;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ScanParams;

import java.util.List;

/**
 * Created by JinJian on 17-8-2.
 */
@Service
public class ClientOpsServiceImpl implements ClientOpsService {

    @Override
    public List<String> scanKeys(String conn, String pattern, Integer cursor, Integer size) throws NonClientExcept {
        ScanParams scanParams = new ScanParams()
                .match(pattern)
                .count(size);

        return SingletonContainer.getSingleton()
                .getJedisResource(conn)
                .scan(cursor, scanParams)
                .getResult();
    }

}
