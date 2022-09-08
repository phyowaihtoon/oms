package com.hmm.dms.web.rest;

import com.hmm.dms.service.DocumentInquiryService;
import com.hmm.dms.service.dto.DocumentDTO;
import com.hmm.dms.service.dto.DocumentHeaderDTO;
import com.hmm.dms.service.dto.ReplyMessage;
import java.io.IOException;
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
    public ResponseEntity<List<DocumentHeaderDTO>> getAllDocumentHeaders(@RequestBody DocumentHeaderDTO dto, Pageable pageable) {
        log.debug("REST request to get all Documents");
        Page<DocumentHeaderDTO> page = documentInquiryService.searchDocumentHeaderByMetaData(dto, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/docinquiry/{id}")
    public DocumentHeaderDTO getAllDocuments(@PathVariable Long id) {
        log.debug("REST request to get all Documents");
        DocumentHeaderDTO headerDTO = documentInquiryService.findAllDocumentsByHeaderId(id);
        return headerDTO;
    }

    @GetMapping("/docinquiry/download/{docId}")
    public ResponseEntity<?> downloadFile(@PathVariable Long docId) {
        log.debug("REST request to get all Documents");
        DocumentDTO docDTO = documentInquiryService.getDocumentById(docId);
        String filePath = docDTO.getFilePath();
        String[] directories = filePath.split("/");
        String fileName = directories[directories.length - 1];
        int dot = fileName.lastIndexOf('.');
        String extension = (dot == -1) ? "" : fileName.substring(dot + 1);
        ReplyMessage<ByteArrayResource> message = null;
        try {
            message = documentInquiryService.downloadFileFromFTPServer(filePath);
        } catch (IOException e) {
            return ResponseEntity.status(204).build();
        } catch (Exception e) {
            return ResponseEntity.status(205).build();
        }

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName);
        return ResponseEntity
            .ok()
            .headers(header)
            //.contentLength(file.length())
            .contentType(MediaType.parseMediaType("application/" + extension))
            .body(message.getData());
    }
}
