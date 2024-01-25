package com.nayaragaspar.gprfid.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nayaragaspar.gprfid.model.Antena;
import com.nayaragaspar.gprfid.model.Log;
import com.nayaragaspar.gprfid.model.dto.AntenaCacheDto;
import com.nayaragaspar.gprfid.model.enums.TipoLogEnum;
import com.nayaragaspar.gprfid.model.enums.TipoOperacaoEnum;
import com.nayaragaspar.gprfid.service.AntenaService;
import com.nayaragaspar.gprfid.service.LogService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("antenas")
@Tag(name = "Antenas")
public class AntenaController {
    private final AntenaService antenaService;
    private final LogService logService;

    public AntenaController(AntenaService antenaService, LogService logService) {
        this.antenaService = antenaService;
        this.logService = logService;
    }

    @Operation(summary = "Buscar antenas")
    @GetMapping("/status")
    public ResponseEntity<List<AntenaCacheDto>> findAll() {
        return ResponseEntity.ok(antenaService.getAllWithStatus());
    }

    @Operation(summary = "Reconectar antena")
    @PostMapping("/reconnect")
    public ResponseEntity<Boolean> reconnect(@RequestParam String ip) {
        boolean result = antenaService.reconnect(ip);

        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString()
                .toUpperCase();
        Log log = new Log(null, ip, TipoOperacaoEnum.INFO, TipoLogEnum.CONEXAO_ANTENA,
                "Conectado manualmente pelo usuário!", username,
                username);
        logService.save(log);

        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Desconectar antena")
    @PostMapping("/disconnect")
    public ResponseEntity<Boolean> disconnect(@RequestParam String ip) {
        boolean result = antenaService.disconnect(ip);

        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString()
                .toUpperCase();
        Log log = new Log(null, ip, TipoOperacaoEnum.ERRO, TipoLogEnum.CONEXAO_ANTENA,
                "Desconectado manualmente pelo usuário!", username,
                username);
        logService.save(log);

        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Buscar antenas vinculadas a pontos de controle")
    @GetMapping
    public ResponseEntity<List<Antena>> findAllBd() {
        return ResponseEntity.ok(antenaService.getAllToConnect());
    }
}
