package com.nayaragaspar.gprfid.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.thingmagic.ReadExceptionListener;
import com.thingmagic.Reader;
import com.thingmagic.ReaderException;
import com.thingmagic.SerialTransportTCP;
import com.thingmagic.SimpleReadPlan;
import com.thingmagic.TMConstants;
import com.thingmagic.TagProtocol;

import com.nayaragaspar.gprfid.exception.CustomBadRequestException;
import com.nayaragaspar.gprfid.exception.NotFoundException;
import com.nayaragaspar.gprfid.listener.ReaderExceptionListener;
import com.nayaragaspar.gprfid.listener.TagsListener;
import com.nayaragaspar.gprfid.model.Antena;
import com.nayaragaspar.gprfid.model.dto.AntenaCacheDto;
import com.nayaragaspar.gprfid.producer.ExceptionProducer;
import com.nayaragaspar.gprfid.producer.TagProducer;
import com.nayaragaspar.gprfid.repository.AntenaCacheRepository;
import com.nayaragaspar.gprfid.repository.AntenaRepository;
import com.nayaragaspar.gprfid.utility.Utility;
import redis.clients.jedis.JedisPooled;

@Service
public class AntenaService {
    private final ExceptionProducer exceptionProducer;
    private final TagProducer tagProducer;
    private final AntenaCacheRepository antenaCacheRepository;
    private final AntenaRepository antenaRepository;
    private final JedisPooled jedis;

    private List<Reader> readers;

    @Value("${tag.read.expires.seconds}")
    private String expiresTagTime;

    public AntenaService(ExceptionProducer exceptionProducer, TagProducer tagProducer,
            AntenaCacheRepository antenaCacheRepository, AntenaRepository antenaRepository,
            JedisPooled jedis, List<Reader> readers) {
        this.exceptionProducer = exceptionProducer;
        this.tagProducer = tagProducer;
        this.antenaCacheRepository = antenaCacheRepository;
        this.antenaRepository = antenaRepository;
        this.jedis = jedis;
        this.readers = readers;
    }

    public List<AntenaCacheDto> getAllWithStatus() {
        System.out.println("Buscar todas antenas com status.");
        return antenaCacheRepository.getAll("*");
    }

    public List<Antena> getAllToConnect() {
        System.out.println("Buscar todas as antenas para conectar");

        return antenaRepository.findByInStatusPontoControle();
    }

    public List<Antena> getByIp(String ip) {
        System.out.println("Buscar antena por IP");

        return antenaRepository.findByDsIpAntena(Utility.getIpWithoutTcp(ip));
    }

    public boolean reconnect(String uri) {
        System.out.println("Reconnect: ");

        List<Antena> antenas = getByIp(uri);

        if (antenas.isEmpty())
            throw new NotFoundException("Antena não encontrada");

        uri = Utility.getIpWithTcp(uri);
        AntenaCacheDto antena = new AntenaCacheDto(antenas.get(0), "off");
        antenaCacheRepository.save(antena);

        try {
            boolean readerFound = false;

            for (Reader reader : readers) {
                if (uri.equals(reader.paramGet(TMConstants.TMR_PARAM_READER_URI))) {
                    continuousConnection(uri);
                    readerFound = true;
                }
            }

            if (!readerFound) {
                readers.add(continuousConnection(uri));
            }

            antena.setStatus("on");
            antenaCacheRepository.save(antena);
        } catch (ReaderException e) {
            System.err.println(e.getMessage());
            antenaCacheRepository.saveIfNotExists(antena);
            return false;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            antenaCacheRepository.saveIfNotExists(antena);
            return false;
        }

        return true;
    }

    public boolean disconnect(String uri) {
        System.out.println("Disconnect: ");

        List<Antena> antenas = getByIp(Utility.getIpWithoutTcp(uri));

        if (antenas.isEmpty()) {
            System.err.println("Antena não encontrada");
            throw new NotFoundException("Antena não encontrada");
        }

        uri = Utility.getIpWithTcp(uri);

        int i = 0;
        int indexToRemove = -1;

        for (Reader reader : readers) {
            try {
                String readerUri = reader.paramGet(TMConstants.TMR_PARAM_READER_URI).toString();

                if (uri.equals(readerUri)) {
                    System.out.println("Desconectar: " + readerUri);
                    readers.get(i).removeReadExceptionListener(null);
                    readers.get(i).removeReadListener(null);
                    readers.get(i).destroy();
                    indexToRemove = i;

                    AntenaCacheDto antena = new AntenaCacheDto(antenas.get(0), "off");
                    antenaCacheRepository.save(antena);
                    System.err.println("Antena desconectada!");
                }
            } catch (ReaderException e) {
                System.err.println(e.getMessage());
                return false;
            } catch (Exception e) {
                System.err.println(e.getMessage());
                return false;
            }

            i++;
        }

        if (indexToRemove != -1) {
            readers.remove(indexToRemove);
        }

        return true;
    }

    public boolean remove(String uri) {
        System.out.println("Remover conexao: " + uri);

        uri = Utility.getIpWithTcp(uri);

        int i = 0;
        int indexToRemove = -1;

        antenaCacheRepository.deleteMaches(uri);

        for (Reader reader : readers) {
            try {
                String readerUri = reader.paramGet(TMConstants.TMR_PARAM_READER_URI).toString();

                if (uri.equals(readerUri)) {
                    System.out.println("Desconectar: " + readerUri);
                    readers.get(i).removeReadExceptionListener(null);
                    readers.get(i).removeReadListener(null);
                    readers.get(i).destroy();
                    indexToRemove = i;

                    System.err.println("Antena desconectada!");
                }
            } catch (ReaderException e) {
                System.err.println(e.getMessage());
                return false;
            } catch (Exception e) {
                System.err.println(e.getMessage());
                return false;
            }

            i++;
        }

        if (indexToRemove != -1) {
            readers.remove(indexToRemove);
        }

        return true;
    }

    private Reader continuousConnection(String ip) {
        try {

            Reader.setSerialTransport("tcp", new SerialTransportTCP.Factory());
            System.out.println("Conecting to " + ip);
            Reader reader = Reader.create(ip);
            reader.connect();

            System.out.println("Conected to " + ip);

            SimpleReadPlan plan = new SimpleReadPlan(new int[] { 1 }, TagProtocol.GEN2);
            reader.paramSet(TMConstants.TMR_PARAM_READ_PLAN, plan);

            TagsListener tagReads = new TagsListener(tagProducer, jedis, antenaCacheRepository, expiresTagTime);
            reader.addReadListener(tagReads);

            ReadExceptionListener exceptionListener = new ReaderExceptionListener(exceptionProducer);
            reader.addReadExceptionListener(exceptionListener);

            reader.startReading();

            System.out.println("lendo tags de: " + ip);

            return reader;
        } catch (ReaderException e) {
            System.out.println("Erro de Conexão: " + e.getMessage());

            if (e.getMessage().contains("Connection refused")) {
                throw new CustomBadRequestException("Conexão recusada");
            }

            System.out.println("Tentar novamente");
            return continuousConnection(ip);
        } catch (Exception e) {
            System.err.println("Erro de Conexão: " + e);
            throw e;
        }
    }

    public void multiConnection(List<Antena> antenasList) {
        try {
            readers = new ArrayList<>();

            antenasList.forEach(this::connect);

        } catch (Exception re) {
            System.err.println("Exception: " + re.getMessage());

            re.printStackTrace();
        }
    }

    private void connect(Antena antenaDto) {
        AntenaCacheDto antenaCacheDto = new AntenaCacheDto(antenaDto, "off");
        antenaCacheRepository.save(antenaCacheDto);

        Reader reader = continuousConnection(antenaCacheDto.getDsIpAntena());
        readers.add(reader);

        antenaCacheDto.setStatus("on");
        antenaCacheRepository.save(antenaCacheDto);
    }

    public Reader connectToWrite(String ip) {
        try {
            Reader.setSerialTransport("tcp", new SerialTransportTCP.Factory());
            Reader reader = Reader.create(ip);
            System.out.println("Conecting in " + ip);

            reader.connect();
            System.out.println("Conected...");

            SimpleReadPlan plan = new SimpleReadPlan(new int[] { 1 }, TagProtocol.GEN2, null, null, 1000);
            reader.paramSet(TMConstants.TMR_PARAM_READ_PLAN, plan);
            reader.paramSet(TMConstants.TMR_PARAM_TAGOP_ANTENNA, 1);
            System.out.println("Params set");

            return reader;
        } catch (ReaderException e) {
            System.err.println("Erro de conexão: " + e.getMessage());

            throw new CustomBadRequestException("Não foi possível estabelecer conexão com a antena!");
        }
    }

    public void disconnectAll() {
        System.out.println("Disconnect all ");

        for (Reader reader : readers) {
            try {
                String antenaUri = reader.paramGet(TMConstants.TMR_PARAM_READER_URI).toString();
                antenaCacheRepository.changeStatus(antenaUri, "off");

                reader.removeReadExceptionListener(null);
                reader.removeReadListener(null);
                reader.destroy();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
