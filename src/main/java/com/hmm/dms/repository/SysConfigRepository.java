package com.hmm.dms.repository;

import com.hmm.dms.domain.SysConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SysConfigRepository extends JpaRepository<SysConfig, Long> {
    SysConfig findByCode(String code);
}
