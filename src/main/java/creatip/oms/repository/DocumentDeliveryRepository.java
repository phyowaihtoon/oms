package creatip.oms.repository;

import creatip.oms.domain.DocumentDelivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Department entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentDeliveryRepository extends JpaRepository<DocumentDelivery, Long> {}
