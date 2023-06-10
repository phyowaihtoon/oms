package creatip.oms.service;

import creatip.oms.service.dto.MetaDataDTO;
import creatip.oms.service.dto.MetaDataHeaderDTO;
import creatip.oms.service.message.BaseMessage;
import creatip.oms.service.message.MetaDataInquiryMessage;
import creatip.oms.service.message.ReplyMessage;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link creatip.oms.domain.MetaData}.
 */
public interface MetaDataService {
    /**
     * Save a metaData.
     *
     * @param metaDataDTO the entity to save.
     * @return the persisted entity.
     */
    MetaDataHeaderDTO save(MetaDataHeaderDTO metaDataDTO);

    ReplyMessage<MetaDataHeaderDTO> saveMetaDataHeader(MetaDataHeaderDTO metaDataDTO);

    /**
     * Partially updates a metaData.
     *
     * @param metaDataDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MetaDataDTO> partialUpdate(MetaDataDTO metaDataDTO);

    /**
     * Get all the metaData.
     *
     * @return the list of entities.
     */
    List<MetaDataHeaderDTO> findAll();

    /**
     * Get the "id" metaData.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MetaDataHeaderDTO> findOne(Long id);

    /**
     * Delete the "id" metaData.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Page<MetaDataHeaderDTO> findAll(Pageable pageable);

    Page<MetaDataHeaderDTO> getAllMetaData(MetaDataInquiryMessage message, Pageable pageable);

    Page<MetaDataHeaderDTO> getAllMetaDataInTrashBin(MetaDataInquiryMessage message, Pageable pageable);

    BaseMessage restoreMetaData(Long id);

    BaseMessage deleteById(Long id);

    BaseMessage deleteHeaderById(Long id);

    List<MetaDataHeaderDTO> findAllMetaData();
}
