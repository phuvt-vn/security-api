package com.example.main.service;

import com.example.main.entity.RedisToken;
import com.example.main.util.HmacUtil;
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

    private static final String HMAC_SECRET = "HMAC_SECRET";

    public String store (RedisToken token) {
        try {
            var tokenId = SecureStringUtil.randomString(30);
            var tokenJson = objectMapper.writeValueAsString(token);
            connection.sync().set(tokenId, tokenJson);
            connection.sync().expire(tokenId, 15*60);

            var hmac= HmacUtil.hmac(tokenId, HMAC_SECRET);

            return StringUtils.join(tokenId,".",hmac);
        } catch (Exception e) {
            e.printStackTrace();
            return StringUtils.EMPTY;
        }
    }

    public Optional<RedisToken> read (String bearerToken) {

        try {
            var tokens = StringUtils.split(bearerToken, ".");

            if(!HmacUtil.isHmacMatch(tokens[0],HMAC_SECRET,tokens[1])){
                return Optional.empty();
            }

            var tokenJson = connection.sync().get(tokens[0]);
            if (StringUtils.isBlank(tokenJson)) {
                return Optional.empty();
            }

            var token = objectMapper.readValue(tokenJson, RedisToken.class);
            return Optional.of(token);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public void delete (String tokenId) {
        connection.sync().del(tokenId);
    }

}
