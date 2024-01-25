package com.nayaragaspar.gprfid.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.nayaragaspar.gprfid.model.dto.AntenaCacheDto;
import com.nayaragaspar.gprfid.model.dto.ExceptionCacheDto;
import redis.clients.jedis.JedisPooled;

@Repository
public class AntenaCacheRepository {
    private final JedisPooled jedis;

    public static final String EXCEPTION_HASH = "exceptionAntena:%s";
    public static final String KEY_HASH = "antena:%s";

    public AntenaCacheRepository(JedisPooled jedis) {
        this.jedis = jedis;
    }

    public void save(AntenaCacheDto antena) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> mapAntenna = objectMapper
                .convertValue(antena, new TypeReference<Map<String, String>>() {
                });

        String key = String.format(KEY_HASH, antena.getDsIpAntena());
        jedis.hmset(key, mapAntenna);
    }

    public void saveException(ExceptionCacheDto exception) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> map = objectMapper
                .convertValue(exception, new TypeReference<Map<String, String>>() {
                });

        String key = String.format(EXCEPTION_HASH, exception.getUri());

        jedis.hmset(key, map);
        jedis.expire(key, 60);
    }

    public void saveIfNotExists(AntenaCacheDto antena) {
        String key = String.format(KEY_HASH, antena.getDsIpAntena());
        Set<String> keys = jedis.keys(key);

        if (keys.isEmpty()) {
            save(antena);
        }
    }

    public void deleteMaches(String keyPattern) {
        String pattern = String.format(KEY_HASH, keyPattern);

        Set<String> keys = jedis.keys(pattern);

        for (String key : keys) {
            jedis.del(key);
        }
    }

    public AntenaCacheDto get(String uri) {
        ObjectMapper objectMapper = new ObjectMapper();
        String key = String.format(KEY_HASH, uri);

        Map<String, String> antenaMap = jedis.hgetAll(key);

        return objectMapper.convertValue(antenaMap, AntenaCacheDto.class);
    }

    public ExceptionCacheDto getException(String uri) {
        ObjectMapper objectMapper = new ObjectMapper();
        String key = String.format(EXCEPTION_HASH, uri);

        Map<String, String> antenaMap = jedis.hgetAll(key);

        return objectMapper.convertValue(antenaMap, ExceptionCacheDto.class);
    }

    public List<AntenaCacheDto> getAll(String keyPattern) {
        List<AntenaCacheDto> result = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        String pattern = String.format(KEY_HASH, keyPattern);
        Set<String> keys = jedis.keys(pattern);

        for (String key : keys) {
            Map<String, String> antenaMap = jedis.hgetAll(key);
            AntenaCacheDto antenaCacheDto = objectMapper.convertValue(antenaMap, AntenaCacheDto.class);
            result.add(antenaCacheDto);
        }

        return result;
    }

    public void changeStatus(String uri, String newStatus) {
        ObjectMapper objectMapper = new ObjectMapper();

        String key = String.format(KEY_HASH, uri);
        Map<String, String> antenaMap = jedis.hgetAll(key);

        AntenaCacheDto antenaCacheDto = objectMapper.convertValue(antenaMap, AntenaCacheDto.class);

        antenaCacheDto.setStatus(newStatus);
        save(antenaCacheDto);
    }
}
