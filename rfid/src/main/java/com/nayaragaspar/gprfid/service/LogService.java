package com.nayaragaspar.gprfid.service;

import org.springframework.stereotype.Service;

import com.nayaragaspar.gprfid.model.Log;
import com.nayaragaspar.gprfid.repository.LogRepository;

@Service
public class LogService {
    private final LogRepository logRepository;

    public LogService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public Log save(Log logDto) {
        return logRepository.save(logDto);
    }
}
