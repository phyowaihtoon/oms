package com.hmm.dms.service.impl;

import com.hmm.dms.domain.SysConfig;
import com.hmm.dms.repository.SysConfigRepository;
import com.hmm.dms.service.SysConfigService;
import com.hmm.dms.service.message.ReplyMessage;
import com.hmm.dms.service.message.SysConfigMessage;
import com.hmm.dms.util.ResponseCode;
import com.hmm.dms.util.SysConfigVariables;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Scanner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SysConfigServiceImpl implements SysConfigService {

    private final SysConfigRepository sysConfigRepository;

    public SysConfigServiceImpl(SysConfigRepository sysConfigRepository) {
        this.sysConfigRepository = sysConfigRepository;
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
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter file path (For e.g, C:\\Folder\\) - ");
        String filePath = sc.nextLine();

        File file = new File(filePath + "test.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;

        while ((st = br.readLine()) != null) {
            System.out.println(st);

            File folder = new File(st);
            File[] listOfFiles = folder.listFiles();

            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    String newFileName = fileNameUpdate(1, listOfFiles[i].getName());
                    File newFile = new File(st + "\\" + newFileName);
                    listOfFiles[i].renameTo(newFile);
                    System.out.println("File " + listOfFiles[i].getName());
                } else if (listOfFiles[i].isDirectory()) {
                    System.out.println("Directory " + listOfFiles[i].getName());
                }
            }
        }
    }

    public static String fileNameUpdate(int versionNo, String filename) {
        String fileVersion = "";

        String[] originalFilearr = filename.split("\\.");
        fileVersion = originalFilearr[0] + "_V" + Integer.toString(versionNo) + "." + originalFilearr[1];

        return fileVersion;
    }
}
