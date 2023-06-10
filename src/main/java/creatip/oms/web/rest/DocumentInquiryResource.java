package creatip.oms.web.rest;

import creatip.oms.service.DocumentInquiryService;
import creatip.oms.service.dto.DocumentDTO;
import creatip.oms.service.dto.DocumentHeaderDTO;
import creatip.oms.service.message.DocumentInquiryMessage;
import creatip.oms.service.message.ReplyMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

@RestController
@RequestMapping("/api")
public class DocumentInquiryResource {

    private final Logger log = LoggerFactory.getLogger(DocumentResource.class);

    private final DocumentInquiryService documentInquiryService;

    public DocumentInquiryResource(DocumentInquiryService documentInquiryService) {
        this.documentInquiryService = documentInquiryService;
    }

    @PostMapping("/docinquiry")
    public ResponseEntity<List<DocumentHeaderDTO>> getAllDocumentHeaders(@RequestBody DocumentInquiryMessage dto, Pageable pageable) {
        log.debug("REST request to get all Documents");
        Page<DocumentHeaderDTO> page = documentInquiryService.findAllDocumentHeaderByMetaData(dto, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PostMapping("/docinquiry/trashbin")
    public ResponseEntity<List<DocumentHeaderDTO>> getAllDocumentHeadersInTrashBin(
        @RequestBody DocumentInquiryMessage dto,
        Pageable pageable
    ) {
        log.debug("REST request to get all Documents");
        Page<DocumentHeaderDTO> page = documentInquiryService.searchDocumentHeaderInTrashBin(dto, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/docinquiry/trashbin/{id}")
    public DocumentHeaderDTO findDeletedDocumentsByHeaderId(@PathVariable Long id) {
        log.debug("REST request to get all deleted Documents");
        DocumentHeaderDTO headerDTO = documentInquiryService.findDeletedDocumentsByHeaderId(id);
        return headerDTO;
    }

    @PostMapping("/docinquiry/servicequeue")
    public ResponseEntity<List<DocumentHeaderDTO>> getAllDocumentHeadersForServiceQueue(
        @RequestBody DocumentInquiryMessage dto,
        Pageable pageable
    ) {
        log.debug("REST request to get all Documents");
        Page<DocumentHeaderDTO> page = documentInquiryService.searchDocumentHeaderForServiceQueue(dto, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/docinquiry/{id}")
    public DocumentHeaderDTO findAllDocumentsByHeaderId(@PathVariable Long id) {
        log.debug("REST request to get all Documents");
        DocumentHeaderDTO headerDTO = documentInquiryService.findAllDocumentsByHeaderId(id);
        return headerDTO;
    }

    @GetMapping("/docinquiry/download/{docId}")
    public ResponseEntity<?> downloadFile(@PathVariable Long docId) {
        log.debug("REST request to download file");

        DocumentDTO docDTO = documentInquiryService.getDocumentById(docId);
        String filePath = docDTO.getFilePath();
        String fileName = docDTO.getFileNameVersion();
        int dot = fileName.lastIndexOf('.');
        String extension = (dot == -1) ? "" : fileName.substring(dot + 1);
        if (extension == null || extension.isEmpty()) {
            System.out.println("Invalid file extension while downloading file");
            return ResponseEntity.status(205).build();
        }

        ReplyMessage<ByteArrayResource> message = null;
        try {
            message = documentInquiryService.downloadFileFromFTPServer(filePath + "//" + fileName);
        } catch (IOException ex) {
            System.out.println("Failed to download: [" + ex.getMessage() + "]");
            return ResponseEntity.status(204).build();
        } catch (Exception ex) {
            System.out.println("Failed to download: [" + ex.getMessage() + "]");
            return ResponseEntity.status(206).build();
        }

        /* Giving file name "abc" is to avoid character encoding issue for Myanmar font */
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=abc" + extension);
        header.setContentType(new MediaType("application", extension, StandardCharsets.UTF_8));
        return ResponseEntity
            .ok()
            .headers(header)
            //.contentLength(file.length())
            .body(message.getData());
    }

    @GetMapping("/docinquiry/preview/{docId}")
    public ResponseEntity<?> previewFile(@PathVariable Long docId) {
        log.debug("REST request to download file");

        DocumentDTO docDTO = documentInquiryService.getDocumentById(docId);
        String filePath = docDTO.getFilePath();
        String fileName = docDTO.getFileNameVersion();
        int dot = fileName.lastIndexOf('.');
        String extension = (dot == -1) ? "" : fileName.substring(dot + 1);
        if (extension == null || extension.isEmpty()) {
            System.out.println("Invalid file extension while downloading file");
            return ResponseEntity.status(205).build();
        }

        ReplyMessage<ByteArrayResource> message = null;
        try {
            message = documentInquiryService.downloadPreviewFile(filePath + "//" + fileName);
        } catch (IOException ex) {
            System.out.println("Failed to download: [" + ex.getMessage() + "]");
            return ResponseEntity.status(204).build();
        } catch (Exception ex) {
            System.out.println("Failed to download: [" + ex.getMessage() + "]");
            return ResponseEntity.status(206).build();
        }

        /* Giving file name "abc" is to avoid character encoding issue for Myanmar font */
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=abc" + extension);
        header.setContentType(new MediaType("application", extension, StandardCharsets.UTF_8));
        return ResponseEntity
            .ok()
            .headers(header)
            //.contentLength(file.length())
            .body(message.getData());
    }
}
