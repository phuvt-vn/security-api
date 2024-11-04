package com.example.main.service;

import com.example.main.entity.RedisToken;
import com.example.main.util.SecureStringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.api.StatefulRedisConnection;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RedisTokenService {

    @Autowired
    private StatefulRedisConnection<String, String> connection;

    @Autowired
    private ObjectMapper objectMapper;

    public String store (RedisToken token) {
        try {
            var tokenId = SecureStringUtil.randomString(30);
            var tokenJson = objectMapper.writeValueAsString(token);
            connection.sync().set(tokenId, tokenJson);
            connection.sync().expire(tokenId, 15*60);
            return tokenId;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return StringUtils.EMPTY;
        }
    }

    public Optional<RedisToken> read (String tokenId) {
        var tokenJson = connection.sync().get(tokenId);
        if (StringUtils.isBlank(tokenJson)) {
            return Optional.empty();
        }
        try {
            var token = objectMapper.readValue(tokenJson, RedisToken.class);
            return Optional.of(token);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public void delete (String tokenId) {
        connection.sync().del(tokenId);
    }

}
