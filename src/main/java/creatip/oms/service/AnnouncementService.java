package creatip.oms.service;

import creatip.oms.service.dto.AnnouncementDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link creatip.oms.domain.Announcement}.
 */
public interface AnnouncementService {
    /**
     * Save an announcement.
     *
     * @param announcementDTO the entity to save.
     * @return the persisted entity.
     */
    AnnouncementDTO save(AnnouncementDTO announcementDTO);

    /**
     * Partially updates an announcement.
     *
     * @param departmentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AnnouncementDTO> partialUpdate(AnnouncementDTO announcementDTO);

    /**
     * Get all the announcements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AnnouncementDTO> findAll(Pageable pageable);

    /**
     * Get the "id" announcement.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AnnouncementDTO> findOne(Long id);

    /**
     * Delete the "id" announcement.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
