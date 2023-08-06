package creatip.oms.repository;

import creatip.oms.domain.DashboardTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DashboardRepository entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DashboardTemplateRepository extends JpaRepository<DashboardTemplate, Long> {}
