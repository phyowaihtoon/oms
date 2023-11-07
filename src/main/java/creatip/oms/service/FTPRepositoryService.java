package creatip.oms.service;

import creatip.oms.service.message.ReplyMessage;
import creatip.oms.util.FTPSessionFactory;
import creatip.oms.util.ResponseCode;
import creatip.oms.util.SysConfigVariables;
import creatip.oms.web.rest.DocumentDeliveryResource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.integration.ftp.session.FtpSession;
import org.springframework.stereotype.Service;

@Service
public class FTPRepositoryService {

    private final Logger logger = LoggerFactory.getLogger(FTPRepositoryService.class);

    @Autowired
    private FTPSessionFactory ftpSessionFactory;

    public ReplyMessage<ByteArrayResource> downloadFile(String filePath) {
        ReplyMessage<ByteArrayResource> replyMessage = new ReplyMessage<ByteArrayResource>();
        FtpSession ftpSession = null;
        ByteArrayOutputStream outputStream = null;

        try {
            ftpSession = this.ftpSessionFactory.getSession();
            logger.info("Connected to FTP Server successfully");
            logger.info("Start downloading file: [{}]", filePath);

            if (!ftpSession.exists(filePath)) {
                logger.debug("File Not Found [{}]", filePath);
                replyMessage.setCode(ResponseCode.ERROR_E00);
                replyMessage.setMessage(String.format("File Not Found [%s]", filePath));
                return replyMessage;
            }

            outputStream = new ByteArrayOutputStream();
            ftpSession.read(filePath, outputStream);
            ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());

            replyMessage.setCode(ResponseCode.SUCCESS);
            replyMessage.setData(resource);
            ftpSession.close();

            logger.info("Downloaded Successfully: [{}]", filePath);
        } catch (IOException ex) {
            logger.error("IOException:", ex);
            replyMessage.setCode(ResponseCode.EXCEP_EX);
            replyMessage.setMessage(ex.getMessage());
        } catch (Exception ex) {
            logger.error("Exception:", ex);
            replyMessage.setCode(ResponseCode.EXCEP_EX);
            replyMessage.setMessage(String.format("Error while downloading [%s]", ex.getMessage()));
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException ex) {
                    logger.error("Error while closing Output Stream :", ex);
                }
            }
        }

        return replyMessage;
    }

    public ReplyMessage<ByteArrayResource> getPreviewFileData(String filePath) {
        ReplyMessage<ByteArrayResource> replyMessage = new ReplyMessage<ByteArrayResource>();
        FtpSession ftpSession = null;
        PDDocument orgPDDDocument = null;
        ByteArrayOutputStream outputStream = null;

        try {
            ftpSession = this.ftpSessionFactory.getSession();

            logger.info("Connected to FTP Server successfully");
            logger.info("Start downloading file: [{}]", filePath);

            if (!ftpSession.exists(filePath)) {
                logger.debug("File Not Found [{}]", filePath);
                replyMessage.setCode(ResponseCode.ERROR_E00);
                replyMessage.setMessage(String.format("File Not Found [%s]", filePath));
                return replyMessage;
            }

            outputStream = new ByteArrayOutputStream();

            if (SysConfigVariables.PDF_PREVIEW_LIMIT_ENABLED.equals("Y") && SysConfigVariables.PDF_PREVIEW_VALUE != null) {
                int previewPageLimit = Integer.parseInt(SysConfigVariables.PDF_PREVIEW_VALUE);
                InputStream inputStream = ftpSession.readRaw(filePath);
                orgPDDDocument = PDDocument.load(inputStream);

                if (previewPageLimit >= orgPDDDocument.getNumberOfPages()) {
                    orgPDDDocument.save(outputStream);
                } else {
                    Splitter pdfSplitter = new Splitter();
                    pdfSplitter.setSplitAtPage(previewPageLimit);
                    List<PDDocument> pdfPageList = pdfSplitter.split(orgPDDDocument);

                    //Need to close all PDF documents because of Out of Memory Issue
                    int index = 0;
                    while (index < pdfPageList.size()) {
                        PDDocument pdfDocument = pdfPageList.get(index);
                        //Getting only first PDF_PREVIEW_VALUE pages
                        if (index == 0) {
                            pdfDocument.save(outputStream);
                        }
                        pdfDocument.close();
                        index++;
                    }

                    pdfPageList = null;
                }
            } else ftpSession.read(filePath, outputStream);

            ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());
            replyMessage.setData(resource);
            replyMessage.setCode(ResponseCode.SUCCESS);
            ftpSession.close();

            logger.info("Downloaded Successfully: [{}]", filePath);
        } catch (IOException ex) {
            String message = "IOException happens while getting preview file data. Please, check application logs for details";
            logger.error("IOException while getting preview file data", ex);
            replyMessage.setCode(ResponseCode.EXCEP_EX);
            replyMessage.setMessage(message);
        } catch (NumberFormatException ex) {
            logger.error("Invalid configuration value for PDF_PREVIEW_LIMIT", ex);
            replyMessage.setCode(ResponseCode.EXCEP_EX);
            replyMessage.setMessage("Invalid configuration value for PDF_PREVIEW_LIMIT [" + SysConfigVariables.PDF_PREVIEW_VALUE + "]");
        } catch (Exception ex) {
            String message = "Exception happens while getting preview file data. Please, check application logs for details";
            logger.error("Error while getting preview file data", ex);
            replyMessage.setCode(ResponseCode.EXCEP_EX);
            replyMessage.setMessage(message);
        } finally {
            //Closing PDF Document
            if (orgPDDDocument != null) {
                try {
                    orgPDDDocument.close();
                } catch (IOException ex) {
                    logger.error("Error while closing PDF Document:" + ex);
                }
            }

            //Closing Output Stream
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException ex) {
                    logger.error("Error while closing Output Stream :", ex);
                }
            }
        }

        return replyMessage;
    }
}
