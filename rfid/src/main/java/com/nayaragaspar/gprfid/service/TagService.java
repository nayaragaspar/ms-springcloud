package com.nayaragaspar.gprfid.service;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.thingmagic.Reader;
import com.thingmagic.ReaderException;
import com.thingmagic.TagData;
import com.thingmagic.TagReadData;

import com.nayaragaspar.gprfid.exception.CustomBadRequestException;
import com.nayaragaspar.gprfid.model.dto.Recurso;
import com.nayaragaspar.gprfid.producer.RecursoProducer;
import com.nayaragaspar.gprfid.utility.Utility;

@Service
public class TagService {
    private final AntenaService antenaService;
    private final RecursoProducer antenaProducer;

    public TagService(AntenaService antenaService, RecursoProducer antenaProducer) {
        this.antenaService = antenaService;
        this.antenaProducer = antenaProducer;
    }

    public String writeTag(String ip, String placa) {
        Recurso recurso = antenaProducer.getWriteResource(ip);

        if (Objects.isNull(recurso) || recurso.dsStringConexao().isEmpty())
            throw new CustomBadRequestException("Não foi possível localizar a antena de escrita!");

        String ipAntena = Utility.getIpWithTcp(recurso.dsStringConexao());
        System.out.println("Ip Antena: " + ipAntena);

        Reader reader = antenaService.connectToWrite(ipAntena);

        try {
            TagReadData[] tagReads;

            /* Grava Tag */
            String epcString = "CXP." + placa;
            byte[] epcByte = epcString.getBytes();

            TagData tagData = new TagData(epcByte);
            reader.writeTag(null, tagData);

            System.out.println("Tag gravada: " + epcString);

            /* Lê tag gravada e retorna valor gravado */
            tagReads = reader.read(500);

            TagReadData tag = null;

            while (tagReads.length == 0)
                tagReads = reader.read(500);

            tag = tagReads[0];

            String epcConvertedToString = new String(tag.getTag().epcBytes(), StandardCharsets.UTF_8);

            System.out.println("EPC String: " + epcConvertedToString);
            System.out.println("EPC Byte: " + tag.getTag().epcString());

            reader.destroy();
            return epcConvertedToString;
        } catch (ReaderException e) {
            System.err.println("Erro ao gravar tag: " + e.getMessage());

            String message = "Houve um erro ao escrever Tag!";
            if (e.getMessage().contains("memory overrun")) {
                message = "Mensagem de gravação excedeu a memória da Tag.";
            } else if (e.getMessage().contains("memory locked")) {
                message = "Memória da tag indisponível.";
            } else if (e.getMessage().contains("No tags found")) {
                message = "Tag não encontrada.";
            }

            throw new CustomBadRequestException(message, e);
        }
    }
}
