package com.nayaragaspar.gprfid.listener;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import com.nayaragaspar.gprfid.model.dto.AntenaCacheDto;
import com.nayaragaspar.gprfid.producer.TagProducer;
import com.nayaragaspar.gprfid.repository.AntenaCacheRepository;
import com.nayaragaspar.gprfid.utility.Utility;
import com.thingmagic.ReadListener;
import com.thingmagic.Reader;
import com.thingmagic.ReaderException;
import com.thingmagic.TagReadData;

import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class TagsListener implements ReadListener {

    private final TagProducer tagProducer;
    private final JedisPooled jedis;
    private final AntenaCacheRepository antenaCacheRepository;

    private Long expiresSeconds;
    private static final String TAG_KEY_HASH = "tag:%s/antena:%s";

    public TagsListener(TagProducer tagProducer, JedisPooled jedis, AntenaCacheRepository antenaCacheRepository,
            String expiresSeconds) {
        this.tagProducer = tagProducer;
        this.jedis = jedis;
        this.antenaCacheRepository = antenaCacheRepository;

        this.expiresSeconds = Long.parseLong(expiresSeconds);
    }

    public void tagRead(Reader r, TagReadData tr) {
        String epcString = new String(tr.getTag().epcBytes(), StandardCharsets.UTF_8);
        try {

            if (epcString.startsWith("CXP.")) {
                String uri = r.paramGet("/reader/uri").toString();

                String key = String.format(TAG_KEY_HASH, epcString, Utility.getIpWithoutTcp(uri));
                String redisResult = jedis.get(key);

                if (Objects.isNull(redisResult)) {
                    tagProducer.publishTag(epcString, uri);

                    jedis.set(key, epcString);
                    jedis.expire(key, expiresSeconds);

                    System.out.println("EPC: " + epcString);

                    verificarStatusAntena(uri);
                }
            }
        } catch (ReaderException e) {
            System.err.println("Erro ao ler tag: " + e.getMessage());
        } catch (JedisConnectionException ex) {
            System.err.println("Erro ao conectar ao redis: " + ex.getMessage());

            try {
                String ip = r.paramGet("/reader/uri").toString();

                tagProducer.publishTag(epcString, ip);
            } catch (ReaderException e) {
                System.err.println("Erro ao ler tag: " + e.getMessage());
            }
        }
    }

    private void verificarStatusAntena(String uri) {
        AntenaCacheDto antenaCacheDto = antenaCacheRepository.get(uri);

        if (!Objects.isNull(antenaCacheDto) && antenaCacheDto.getStatus().equals("off")) {
            antenaCacheRepository.changeStatus(uri, "on");
        }
    }
}
