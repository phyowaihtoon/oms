package creatip.oms.repository;

import creatip.oms.domain.MetaDataHeader;
import creatip.oms.domain.RepositoryDomain;
import java.util.stream.Stream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MetaDataHeader entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RepositoryDetailRepository extends JpaRepository<RepositoryDomain, Long> {
    void deleteByHeaderId(Long id);

    Stream<RepositoryDomain> findByHeaderId(Long id);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE repository SET del_flag='Y' " + "WHERE header_id = ?", nativeQuery = true)
    void updateByHeaderId(Long id);
}
