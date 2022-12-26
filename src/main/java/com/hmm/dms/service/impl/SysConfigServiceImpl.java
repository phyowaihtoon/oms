package com.hmm.dms.service.impl;

import com.hmm.dms.domain.Document;
import com.hmm.dms.domain.SysConfig;
import com.hmm.dms.repository.DocumentRepository;
import com.hmm.dms.repository.SysConfigRepository;
import com.hmm.dms.service.SysConfigService;
import com.hmm.dms.service.dto.DocumentDTO;
import com.hmm.dms.service.dto.DocumentHeaderDTO;
import com.hmm.dms.service.mapper.DocumentMapper;
import com.hmm.dms.service.message.ReplyMessage;
import com.hmm.dms.service.message.SysConfigMessage;
import com.hmm.dms.util.FTPSessionFactory;
import com.hmm.dms.util.ResponseCode;
import com.hmm.dms.util.SysConfigVariables;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.integration.ftp.session.FtpSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SysConfigServiceImpl implements SysConfigService {

    private final SysConfigRepository sysConfigRepository;
    private final DocumentRepository documentRepository;
    private final DocumentMapper documentMapper;
    private final FTPSessionFactory ftpSessionFactory;

    public SysConfigServiceImpl(
        SysConfigRepository sysConfigRepository,
        DocumentRepository documentRepository,
        DocumentMapper documentMapper,
        FTPSessionFactory ftpSessionFactory
    ) {
        this.sysConfigRepository = sysConfigRepository;
        this.documentRepository = documentRepository;
        this.documentMapper = documentMapper;
        this.ftpSessionFactory = ftpSessionFactory;
    }

    @Override
    public SysConfigMessage defineSysConfig() {
        SysConfigMessage message = new SysConfigMessage();
        List<SysConfig> configList = this.sysConfigRepository.findAll();
        for (SysConfig data : configList) {
            if (data.getCode().equals(SysConfigVariables.WORKFLOW_AUTHORITY)) {
                SysConfigVariables.WORKFLOW_ENABLED = data.getEnabled();
                message.setWorkflowEnabled(data.getEnabled());
            }
            if (data.getCode().equals(SysConfigVariables.PDF_PREVIEW_LIMIT)) {
                SysConfigVariables.PDF_PREVIEW_ENABLED = data.getEnabled();
                SysConfigVariables.PDF_PREVIEW_VALUE = data.getValue();
            }
        }
        return message;
    }

    @Override
    public ReplyMessage<List<SysConfig>> getAllSysConfig() {
        ReplyMessage<List<SysConfig>> replyMessage = new ReplyMessage<List<SysConfig>>();
        List<SysConfig> configList = this.sysConfigRepository.findAll();
        replyMessage.setCode(ResponseCode.SUCCESS);
        replyMessage.setData(configList);
        return replyMessage;
    }

    @Override
    public ReplyMessage<List<SysConfig>> saveAllSysConfig(List<SysConfig> data) {
        ReplyMessage<List<SysConfig>> replyMessage = new ReplyMessage<List<SysConfig>>();
        List<SysConfig> configList = this.sysConfigRepository.saveAll(data);
        replyMessage.setCode(ResponseCode.SUCCESS);
        replyMessage.setMessage("Updated Successfully");
        replyMessage.setData(configList);
        return replyMessage;
    }

    @Override
    public void updateFileVersion() throws Exception {
        FtpSession ftpSession = this.ftpSessionFactory.getSession();
        System.out.println("Connected successfully to FTP Server");
        List<Document> docList = documentRepository.findAll();
        List<DocumentDTO> documentDTO = documentMapper.toDto(docList);

        for (int i = 0; i < documentDTO.size(); i++) {
            boolean isPathExists = ftpSession.exists("//" + documentDTO.get(i).getFilePath());
            if (isPathExists) {
                long versionExist;
                versionExist = documentRepository.checkVersion(documentDTO.get(i).getId());
                if (versionExist > 0) {
                    FTPFile[] files = ftpSession.list("//" + documentDTO.get(i).getFilePath() + "//" + documentDTO.get(i).getFileName());
                    if (files != null && files.length > 0) {
                        for (FTPFile aFile : files) {
                            String updatedFileName = updateFileNameWithVersion(aFile.getName(), documentDTO.get(i).getHeaderId(), 1);
                            ftpSession.rename(
                                "//" + documentDTO.get(i).getFilePath() + "//" + aFile.getName(),
                                "//" + documentDTO.get(i).getFilePath() + "//" + updatedFileName
                            );
                            documentRepository.update_Version(documentDTO.get(i).getId(), updatedFileName, 1);
                        }
                    }
                } else {
                    return;
                }
            }
        }
    }

    private String updateFileNameWithVersion(String filename, Long headerId, int versionNo) {
        int indexOfDot = filename.lastIndexOf(".");
        String fileNameWithVersion =
            filename.substring(0, indexOfDot) +
            "_HID" +
            Long.toString(headerId) +
            "_V" +
            Integer.toString(versionNo) +
            filename.substring(indexOfDot);
        return fileNameWithVersion;
    }
}
