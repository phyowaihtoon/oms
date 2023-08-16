package creatip.oms.service;

import creatip.oms.service.message.MeetingMessage;
import creatip.oms.service.message.ReplyMessage;
import creatip.oms.service.message.UploadFailedException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface MeetingDeliveryService {
    ReplyMessage<MeetingMessage> save(MeetingMessage meetingMessage, List<MultipartFile> attachedFiles) throws UploadFailedException;
}
