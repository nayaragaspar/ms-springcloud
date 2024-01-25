package com.nayaragaspar.gprfid.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nayaragaspar.gprfid.model.Log;

public interface LogRepository extends JpaRepository<Log, BigDecimal> {

}
