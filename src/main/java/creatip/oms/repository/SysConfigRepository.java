package creatip.oms.repository;

import creatip.oms.domain.SysConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SysConfigRepository extends JpaRepository<SysConfig, Long> {
    SysConfig findByCode(String code);
}
