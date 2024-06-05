package creatip.oms.service.impl;

import creatip.oms.domain.Announcement;
import creatip.oms.domain.DashboardTemplate;
import creatip.oms.domain.Department;
import creatip.oms.domain.HeadDepartment;
import creatip.oms.repository.AnnouncementRepository;
import creatip.oms.repository.DashboardTemplateRepository;
import creatip.oms.repository.DepartmentRepository;
import creatip.oms.repository.DocumentDeliveryRepository;
import creatip.oms.repository.HeadDepartmentRepository;
import creatip.oms.repository.MeetingDeliveryRepository;
import creatip.oms.service.LoadSetupService;
import creatip.oms.service.dto.AnnouncementDTO;
import creatip.oms.service.dto.DashboardTemplateDto;
import creatip.oms.service.dto.DepartmentDTO;
import creatip.oms.service.dto.DraftSummaryDTO;
import creatip.oms.service.dto.HeadDepartmentDTO;
import creatip.oms.service.mapper.AnnouncementMapper;
import creatip.oms.service.mapper.DashboardTemplateMapper;
import creatip.oms.service.mapper.DepartmentMapper;
import creatip.oms.service.mapper.HeadDepartmentMapper;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LoadSetupServiceImpl implements LoadSetupService {

    private final DashboardTemplateRepository dashboardTemplateRepository;
    private final DashboardTemplateMapper dashboardTemplateMapper;

    private final HeadDepartmentRepository headDepartmentRepository;
    private final HeadDepartmentMapper headDepartmentMapper;

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    private final DocumentDeliveryRepository documentDeliveryRepository;
    private final MeetingDeliveryRepository meetingDeliveryRepository;

    private final AnnouncementRepository announcementRepository;
    private final AnnouncementMapper announcementMapper;

    public LoadSetupServiceImpl(
        DashboardTemplateRepository dashboardTemplateRepository,
        DashboardTemplateMapper dashboardTemplateMapper,
        HeadDepartmentRepository headDepartmentRepository,
        HeadDepartmentMapper headDepartmentMapper,
        DepartmentRepository departmentRepository,
        DepartmentMapper departmentMapper,
        DocumentDeliveryRepository documentDeliveryRepository,
        MeetingDeliveryRepository meetingDeliveryRepository,
        AnnouncementRepository announcementRepository,
        AnnouncementMapper announcementMapper
    ) {
        this.dashboardTemplateRepository = dashboardTemplateRepository;
        this.dashboardTemplateMapper = dashboardTemplateMapper;
        this.headDepartmentRepository = headDepartmentRepository;
        this.headDepartmentMapper = headDepartmentMapper;
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
        this.documentDeliveryRepository = documentDeliveryRepository;
        this.meetingDeliveryRepository = meetingDeliveryRepository;
        this.announcementRepository = announcementRepository;
        this.announcementMapper = announcementMapper;
    }

    @Override
    public List<DashboardTemplateDto> loadAllDashboardTemplate() {
        List<DashboardTemplate> entityList = this.dashboardTemplateRepository.findAll();
        return this.dashboardTemplateMapper.toDto(entityList);
    }

    @Override
    public List<HeadDepartmentDTO> getAllHeadDepartments() {
        List<HeadDepartment> entityList = this.headDepartmentRepository.findAll();
        return this.headDepartmentMapper.toDto(entityList);
    }

    @Override
    public List<DepartmentDTO> getAllSubDepartments() {
        List<Department> entityList = this.departmentRepository.findAll();
        return this.departmentMapper.toDto(entityList);
    }

    @Override
    public DraftSummaryDTO getDraftSummary(DepartmentDTO departmentDTO) {
        DraftSummaryDTO draftSummaryDTO = new DraftSummaryDTO();
        Department sender = this.departmentMapper.toEntity(departmentDTO);
        Long deliveryCount = documentDeliveryRepository.countBySenderAndDeliveryStatusAndDelFlag(sender, (short) 0, "N");
        Long meetingCount = meetingDeliveryRepository.countBySenderAndDeliveryStatusAndDelFlag(sender, (short) 0, "N");
        draftSummaryDTO.setDeliveryCount(deliveryCount);
        draftSummaryDTO.setMeetingCount(meetingCount);
        return draftSummaryDTO;
    }

    @Override
    public List<AnnouncementDTO> getAllAnnouncements() {
        List<Announcement> entityList = this.announcementRepository.findByActiveFlagAndDelFlagOrderById("Y", "N");
        return this.announcementMapper.toDto(entityList);
    }

	@Override
	public List<DepartmentDTO> getAllSubDepartmentsByDelFlag(String delFlag) {
		List<Department> entityList = this.departmentRepository.getAllSubDepartmentsByDelFlag("N");
        return this.departmentMapper.toDto(entityList);
	}
}
