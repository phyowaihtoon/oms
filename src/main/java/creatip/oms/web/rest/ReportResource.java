package creatip.oms.web.rest;

import creatip.oms.domain.User;
import creatip.oms.service.ReportService;
import creatip.oms.service.UserService;
import creatip.oms.service.message.ReplyMessage;
import creatip.oms.service.message.RptParamsMessage;
import creatip.oms.util.SharedUtils;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.servlet.ServletContext;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ReportResource {

    private final ReportService reportService;

    @Autowired
    ServletContext context;

    private final UserService userService;

    public ReportResource(ReportService reportService, UserService userService) {
        this.reportService = reportService;
        this.userService = userService;
    }

    //@PathVariable String fileName
    @GetMapping("/viewPdf/{fileName}")
    public ResponseEntity<Resource> viewPdf(@PathVariable final String fileName) throws Exception {
        User loginUser = userService.getUserWithAuthorities().get();
        String rptOutFolder = context.getRealPath("RPT_OUTPUT");
        String rptOutputPath = rptOutFolder + File.separator + loginUser.getLogin() + File.separator;
        File file = new File(rptOutputPath + fileName);
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=" + fileName);
        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
        return ResponseEntity.ok().headers(header).contentLength(file.length()).contentType(MediaType.APPLICATION_PDF).body(resource);
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> download(@PathVariable final String fileName) throws Exception {
        User loginUser = userService.getUserWithAuthorities().get();
        int dot = fileName.lastIndexOf('.');
        String extension = (dot == -1) ? "" : fileName.substring(dot + 1);
        String rptOutFolder = context.getRealPath("RPT_OUTPUT");
        String rptOutputPath = rptOutFolder + File.separator + loginUser.getLogin() + File.separator;
        File file = new File(rptOutputPath + fileName);
        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName);
        return ResponseEntity
            .ok()
            .headers(header)
            .contentLength(file.length())
            .contentType(MediaType.parseMediaType("application/" + extension))
            .body(resource);
    }

    @PostMapping("/docmap-rpt")
    public ReplyMessage<RptParamsMessage> generateDocumentListRpt(@Valid @RequestBody RptParamsMessage rptParams) {
        User loginUser = userService.getUserWithAuthorities().get();
        String rptOutFolder = context.getRealPath("RPT_OUTPUT");
        String rptOutputPath = rptOutFolder + File.separator + loginUser.getLogin() + File.separator;
        String rptFileName = SharedUtils.generateFileName("DocumentMappingRpt");
        rptParams.setRptFileName(rptFileName);
        rptParams.setRptOutputPath(rptOutputPath);
        rptParams.setRptJrxml("DocumentMappingRpt.jrxml");
        rptParams.setRptJasper("DocumentMappingRpt.jrxml");
        ReplyMessage<RptParamsMessage> replyMessage = this.reportService.generateDocumentListRpt(rptParams);
        return replyMessage;
    }

    @PostMapping("/doclist-rpt")
    public ReplyMessage<RptParamsMessage> generateDocumentListRpt2(@Valid @RequestBody RptParamsMessage rptParams) {
        User loginUser = userService.getUserWithAuthorities().get();
        String rptOutFolder = context.getRealPath("RPT_OUTPUT");
        String rptOutputPath = rptOutFolder + File.separator + loginUser.getLogin() + File.separator;
        String rptFileName = SharedUtils.generateFileName("DocumentListRpt");
        rptParams.setRptFileName(rptFileName);
        rptParams.setRptOutputPath(rptOutputPath);
        rptParams.setRptJrxml("DocumentListRpt.jrxml");
        rptParams.setRptJasper("DocumentListRpt.jrxml");
        ReplyMessage<RptParamsMessage> replyMessage = this.reportService.generateUploadedDocumentListRpt(rptParams, loginUser.getId());
        return replyMessage;
    }
}
