package com.hmm.dms.repository;

import com.hmm.dms.domain.DashboardTemplate;
import com.hmm.dms.domain.MetaData;
import com.hmm.dms.domain.PieData;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DashboardRepository entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DashboardTemplateRepository extends JpaRepository<DashboardTemplate, Long> {}
