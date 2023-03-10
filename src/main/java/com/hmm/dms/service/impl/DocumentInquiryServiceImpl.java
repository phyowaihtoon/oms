package com.hmm.dms.service.impl;

import com.hmm.dms.domain.Document;
import com.hmm.dms.domain.DocumentHeader;
import com.hmm.dms.enumeration.CommonEnum.DocumentStatusEnum;
import com.hmm.dms.repository.DocumentHeaderRepository;
import com.hmm.dms.repository.DocumentRepository;
import com.hmm.dms.service.DocumentInquiryService;
import com.hmm.dms.service.dto.DocumentDTO;
import com.hmm.dms.service.dto.DocumentHeaderDTO;
import com.hmm.dms.service.mapper.DocumentHeaderMapper;
import com.hmm.dms.service.mapper.DocumentMapper;
import com.hmm.dms.service.message.DocumentInquiryMessage;
import com.hmm.dms.service.message.ReplyMessage;
import com.hmm.dms.util.FTPSessionFactory;
import com.hmm.dms.util.ResponseCode;
import com.hmm.dms.util.SysConfigVariables;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.integration.ftp.session.FtpSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DocumentInquiryServiceImpl implements DocumentInquiryService {

    private final DocumentHeaderRepository documentHeaderRepository;
    private final DocumentRepository documentRepository;

    private final DocumentHeaderMapper documentHeaderMapper;
    private final DocumentMapper documentMapper;

    @Autowired
    private FTPSessionFactory ftpSessionFactory;

    public DocumentInquiryServiceImpl(
        DocumentHeaderRepository documentHeaderRepository,
        DocumentRepository documentRepository,
        DocumentHeaderMapper documentHeaderMapper,
        DocumentMapper documentMapper
    ) {
        this.documentHeaderRepository = documentHeaderRepository;
        this.documentRepository = documentRepository;
        this.documentHeaderMapper = documentHeaderMapper;
        this.documentMapper = documentMapper;
    }

    @Override
    public Page<DocumentHeaderDTO> searchDocumentHeaderByMetaData(DocumentInquiryMessage dto, Pageable pageable) {
        String specificVal1 = dto.getFieldValue1();
        if (specificVal1 == null || specificVal1.equals("null") || specificVal1.isEmpty()) {
            specificVal1 = "";
            dto.setFieldIndex1(0);
        } else specificVal1 = specificVal1.trim();

        String specificVal2 = dto.getFieldValue2();
        if (specificVal2 == null || specificVal2.equals("null") || specificVal2.isEmpty()) {
            specificVal2 = "";
            dto.setFieldIndex2(0);
        } else specificVal2 = specificVal2.trim();

        String specificVal3 = dto.getFieldValue3();
        if (specificVal3 == null || specificVal3.equals("null") || specificVal3.isEmpty()) {
            specificVal3 = "";
            dto.setFieldIndex3(0);
        } else specificVal3 = specificVal3.trim();

        String generalVal = dto.getGeneralValue();
        if (generalVal == null || generalVal.equals("null") || generalVal.isEmpty()) generalVal = ""; else generalVal = generalVal.trim();

        Set<Integer> setOfStatus = new HashSet<Integer>();
        if (dto.getStatus() == 0) {
            for (DocumentStatusEnum enumData : DocumentStatusEnum.values()) {
                setOfStatus.add(enumData.value);
            }
        } else setOfStatus.add(dto.getStatus());

        if (dto.getCreatedDate() != null && dto.getCreatedDate().trim().length() > 0) {
            String createdDate = dto.getCreatedDate();
            Page<DocumentHeader> pageWithEntity =
                this.documentHeaderRepository.findAllByDate(
                        dto.getMetaDataHeaderId(),
                        dto.getFieldIndex1(),
                        specificVal1,
                        dto.getFieldIndex2(),
                        specificVal2,
                        dto.getFieldIndex3(),
                        specificVal3,
                        generalVal,
                        createdDate,
                        setOfStatus,
                        pageable
                    );
            return pageWithEntity.map(documentHeaderMapper::toDto);
        }

        Page<DocumentHeader> pageWithEntity =
            this.documentHeaderRepository.findAll(
                    dto.getMetaDataHeaderId(),
                    dto.getFieldIndex1(),
                    specificVal1,
                    dto.getFieldIndex2(),
                    specificVal2,
                    dto.getFieldIndex3(),
                    specificVal3,
                    generalVal,
                    setOfStatus,
                    pageable
                );
        return pageWithEntity.map(documentHeaderMapper::toDto);
    }

    @Override
    public Page<DocumentHeaderDTO> searchDocumentHeaderForServiceQueue(DocumentInquiryMessage dto, Pageable pageable) {
        Page<DocumentHeader> pageWithEntity = this.documentHeaderRepository.findByStatus(2, pageable);
        return pageWithEntity.map(documentHeaderMapper::toDto);
    }

    @Override
    public DocumentHeaderDTO findAllDocumentsByHeaderId(Long id) {
        DocumentHeader docHeaderEntity = this.documentHeaderRepository.findDocumentHeaderById(id);
        DocumentHeaderDTO docHeaderDTO = this.documentHeaderMapper.toDto(docHeaderEntity);
        List<Document> docList = this.documentRepository.findAllByHeaderIdAndDelFlag(docHeaderDTO.getId(), "N");
        List<DocumentDTO> docDTOList = this.documentMapper.toDto(docList);
        docHeaderDTO.setDocList(docDTOList);
        return docHeaderDTO;
    }

    @Override
    public DocumentHeaderDTO findDeletedDocumentsByHeaderId(Long id) {
        DocumentHeader docHeaderEntity = this.documentHeaderRepository.findDocumentHeaderById(id);
        DocumentHeaderDTO docHeaderDTO = this.documentHeaderMapper.toDto(docHeaderEntity);
        List<Document> docList = this.documentRepository.findAllByHeaderIdAndDelFlag(docHeaderDTO.getId(), "Y");
        List<DocumentDTO> docDTOList = this.documentMapper.toDto(docList);
        docHeaderDTO.setDocList(docDTOList);
        return docHeaderDTO;
    }

    @Override
    public DocumentDTO getDocumentById(Long id) {
        Optional<Document> document = this.documentRepository.findById(id);
        DocumentDTO docDTO = this.documentMapper.toDto(document.get());
        return docDTO;
    }

    @Override
    public ReplyMessage<ByteArrayResource> downloadFileFromFTPServer(String filePath) throws IOException, Exception {
        ReplyMessage<ByteArrayResource> replyMessage = new ReplyMessage<ByteArrayResource>();
        FtpSession ftpSession = this.ftpSessionFactory.getSession();
        System.out.println("Connected successfully to FTP Server");
        System.out.println("Start downloading file: [" + filePath + "]");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ftpSession.read(filePath, out);
        ByteArrayResource resource = new ByteArrayResource(out.toByteArray());
        replyMessage.setCode(ResponseCode.SUCCESS);
        replyMessage.setData(resource);
        ftpSession.close();

        System.out.println("Downloaded Successfully: [" + filePath + "]");

        return replyMessage;
    }

    @Override
    public Page<DocumentHeaderDTO> searchDocumentHeaderInTrashBin(DocumentInquiryMessage dto, Pageable pageable) {
        String specificVal1 = dto.getFieldValue1();
        if (specificVal1 == null || specificVal1.equals("null") || specificVal1.isEmpty()) {
            specificVal1 = "";
            dto.setFieldIndex1(0);
        } else specificVal1 = specificVal1.trim();

        String specificVal2 = dto.getFieldValue2();
        if (specificVal2 == null || specificVal2.equals("null") || specificVal2.isEmpty()) {
            specificVal2 = "";
            dto.setFieldIndex2(0);
        } else specificVal2 = specificVal2.trim();

        String specificVal3 = dto.getFieldValue3();
        if (specificVal3 == null || specificVal3.equals("null") || specificVal3.isEmpty()) {
            specificVal3 = "";
            dto.setFieldIndex3(0);
        } else specificVal3 = specificVal3.trim();

        String generalVal = dto.getGeneralValue();
        if (generalVal == null || generalVal.equals("null") || generalVal.isEmpty()) generalVal = ""; else generalVal = generalVal.trim();

        //Approved Item is not allowed to restore
        Set<Integer> setOfStatus = new HashSet<Integer>();
        setOfStatus.add(0);
        setOfStatus.add(1);
        setOfStatus.add(2);
        setOfStatus.add(3);
        setOfStatus.add(4);
        setOfStatus.add(6);

        if (dto.getCreatedDate() != null && dto.getCreatedDate().trim().length() > 0) {
            String createdDate = dto.getCreatedDate();
            Page<DocumentHeader> pageWithEntity =
                this.documentHeaderRepository.findTrashItemsByDate(
                        dto.getMetaDataHeaderId(),
                        dto.getFieldIndex1(),
                        specificVal1,
                        dto.getFieldIndex2(),
                        specificVal2,
                        dto.getFieldIndex3(),
                        specificVal3,
                        generalVal,
                        createdDate,
                        setOfStatus,
                        pageable
                    );
            return pageWithEntity.map(documentHeaderMapper::toDto);
        }

        Page<DocumentHeader> pageWithEntity =
            this.documentHeaderRepository.findTrashItems(
                    dto.getMetaDataHeaderId(),
                    dto.getFieldIndex1(),
                    specificVal1,
                    dto.getFieldIndex2(),
                    specificVal2,
                    dto.getFieldIndex3(),
                    specificVal3,
                    generalVal,
                    setOfStatus,
                    pageable
                );
        return pageWithEntity.map(documentHeaderMapper::toDto);
    }

    @Override
    public ReplyMessage<ByteArrayResource> downloadPreviewFile(String filePath) throws IOException, Exception {
        ReplyMessage<ByteArrayResource> replyMessage = new ReplyMessage<ByteArrayResource>();
        FtpSession ftpSession = this.ftpSessionFactory.getSession();
        System.out.println("Connected successfully to FTP Server");
        System.out.println("Start downloading file: [" + filePath + "]");

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        if (SysConfigVariables.PDF_PREVIEW_ENABLED.equals("Y") && SysConfigVariables.PDF_PREVIEW_VALUE != null) {
            try {
                InputStream inputStream = ftpSession.readRaw(filePath);
                PDDocument pdboxDoc = PDDocument.load(inputStream);
                Splitter pdfSplitter = new Splitter();
                pdfSplitter.setSplitAtPage(Integer.parseInt(SysConfigVariables.PDF_PREVIEW_VALUE));

                List<PDDocument> pdfPageList = pdfSplitter.split(pdboxDoc);
                for (PDDocument pdfDoc : pdfPageList) {
                    pdfDoc.save(out);
                    break;
                }
            } catch (Exception ex) {
                System.out.println("Invalid Configuration for PDF_PREVIEW_LIMIT :" + ex.getMessage());
                ex.printStackTrace();
                ftpSession.read(filePath, out);
            }
        } else ftpSession.read(filePath, out);

        ByteArrayResource resource = new ByteArrayResource(out.toByteArray());
        replyMessage.setCode(ResponseCode.SUCCESS);
        replyMessage.setData(resource);
        ftpSession.close();

        System.out.println("Downloaded Successfully: [" + filePath + "]");

        return replyMessage;
    }
}
