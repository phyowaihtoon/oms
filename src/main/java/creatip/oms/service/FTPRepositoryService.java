package creatip.oms.service;

import creatip.oms.service.message.ReplyMessage;
import creatip.oms.util.FTPSessionFactory;
import creatip.oms.util.ResponseCode;
import creatip.oms.util.SysConfigVariables;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.integration.ftp.session.FtpSession;
import org.springframework.stereotype.Service;

@Service
public class FTPRepositoryService {

    @Autowired
    private FTPSessionFactory ftpSessionFactory;

    public ReplyMessage<ByteArrayResource> downloadFile(String filePath) throws IOException, Exception {
        ReplyMessage<ByteArrayResource> replyMessage = new ReplyMessage<ByteArrayResource>();
        FtpSession ftpSession = this.ftpSessionFactory.getSession();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ftpSession.read(filePath, out);
        ByteArrayResource resource = new ByteArrayResource(out.toByteArray());
        replyMessage.setCode(ResponseCode.SUCCESS);
        replyMessage.setData(resource);
        ftpSession.close();

        return replyMessage;
    }

    public ReplyMessage<ByteArrayResource> getPreviewFileData(String filePath) throws IOException, Exception {
        ReplyMessage<ByteArrayResource> replyMessage = new ReplyMessage<ByteArrayResource>();
        FtpSession ftpSession = this.ftpSessionFactory.getSession();

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        if (SysConfigVariables.PDF_PREVIEW_LIMIT_ENABLED.equals("Y") && SysConfigVariables.PDF_PREVIEW_VALUE != null) {
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

        return replyMessage;
    }
}
