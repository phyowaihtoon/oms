package creatip.oms.service.impl;

import creatip.oms.domain.PieData;
import creatip.oms.domain.PieData2;
import creatip.oms.repository.DashboardRepository;
import creatip.oms.repository.DashboardTemplateRepository;
import creatip.oms.repository.MetaDataRepository;
import creatip.oms.service.DashboardService;
import creatip.oms.service.dto.BarDataDto;
import creatip.oms.service.dto.BasicLineDto;
import creatip.oms.service.dto.DashboardTemplateDto;
import creatip.oms.service.dto.InputParamDto;
import creatip.oms.service.dto.LineDataDto;
import creatip.oms.service.dto.MetaDataDTO;
import creatip.oms.service.dto.PieDataDto;
import creatip.oms.service.dto.PieHeaderDataDto;
import creatip.oms.service.mapper.DashboardTemplateMapper;
import creatip.oms.service.mapper.MetaDataMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DashboradServiceImpl implements DashboardService {

    private final Logger log = LoggerFactory.getLogger(MetaDataServiceImpl.class);

    private final DashboardTemplateRepository dashboardTemplateRepository;

    private final DashboardRepository dashboardRepository;

    private final DashboardTemplateMapper dashboardTemplateMapper;

    private final MetaDataRepository metaDataRepository;

    private final MetaDataMapper metaDataMapper;

    public DashboradServiceImpl(
        DashboardTemplateRepository dashboardTemplateRepository,
        DashboardRepository dashboardRepository,
        DashboardTemplateMapper dashboardTemplateMapper,
        MetaDataRepository metaDataRepository,
        MetaDataMapper metaDataMapper
    ) {
        this.dashboardTemplateRepository = dashboardTemplateRepository;
        this.dashboardRepository = dashboardRepository;
        this.dashboardTemplateMapper = dashboardTemplateMapper;
        this.metaDataRepository = metaDataRepository;
        this.metaDataMapper = metaDataMapper;
    }

    @Override
    public List<DashboardTemplateDto> findAll() {
        log.debug("Request to get all Dashboard Template");
        return dashboardTemplateRepository
            .findAll()
            .stream()
            .map(dashboardTemplateMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public Optional<PieHeaderDataDto> getAllSummary() {
        Optional<PieHeaderDataDto> pieHeaderDataDto = Optional.of(new PieHeaderDataDto());
        List<PieData> pieData = dashboardRepository.getAllSummary();
        long totalCount = 0;
        long newCount = 0;
        long approvalCount = 0L;
        long rejectedCount = 0L;
        long amendmentCount = 0L;
        long approvedCount = 0L;
        long cancelCount = 0L;
        for (int i = 0; i < pieData.size(); i++) {
            totalCount += pieData.get(i).getCount();
            if (pieData.get(i).getStatus() == 1) newCount = pieData.get(i).getCount();
            if (pieData.get(i).getStatus() == 2) approvalCount = pieData.get(i).getCount();
            if (pieData.get(i).getStatus() == 3) cancelCount = pieData.get(i).getCount();
            if (pieData.get(i).getStatus() == 4) amendmentCount = pieData.get(i).getCount();
            if (pieData.get(i).getStatus() == 5) approvedCount = pieData.get(i).getCount();
            if (pieData.get(i).getStatus() == 6) rejectedCount = pieData.get(i).getCount();
        }
        pieHeaderDataDto
            .get()
            .getData()
            .add(
                new PieDataDto(
                    "New - " + newCount + (newCount > 1 ? " Records" : " Record"),
                    totalCount > 0 ? (float) (newCount * 100) / totalCount : 0
                )
            );
        pieHeaderDataDto
            .get()
            .getData()
            .add(
                new PieDataDto(
                    "Sent for Approval - " + approvalCount + (approvalCount > 1 ? " Records" : " Record"),
                    totalCount > 0 ? (float) (approvalCount * 100) / totalCount : 0
                )
            );

        pieHeaderDataDto
            .get()
            .getData()
            .add(
                new PieDataDto(
                    "Sent for Amendment - " + amendmentCount + (amendmentCount > 1 ? " Records" : " Record"),
                    totalCount > 0 ? (float) (amendmentCount * 100) / totalCount : 0
                )
            );
        pieHeaderDataDto
            .get()
            .getData()
            .add(
                new PieDataDto(
                    "Canceled - " + cancelCount + (cancelCount > 1 ? " Records" : " Record"),
                    totalCount > 0 ? (float) (cancelCount * 100) / totalCount : 0
                )
            );

        pieHeaderDataDto
            .get()
            .getData()
            .add(
                new PieDataDto(
                    "Approved - " + approvedCount + (approvedCount > 1 ? " Records" : " Record"),
                    totalCount > 0 ? (float) (approvedCount * 100) / totalCount : 0
                )
            );

        pieHeaderDataDto
            .get()
            .getData()
            .add(
                new PieDataDto(
                    "Rejected - " + rejectedCount + (rejectedCount > 1 ? " Records" : " Record"),
                    totalCount > 0 ? (float) (rejectedCount * 100) / totalCount : 0
                )
            );

        pieHeaderDataDto.get().setTotalCount(totalCount);

        return pieHeaderDataDto;
    }

    @Override
    public Optional<PieHeaderDataDto> getTodaySummary() {
        Optional<PieHeaderDataDto> pieHeaderDataDto = Optional.of(new PieHeaderDataDto());
        List<PieData> pieData = dashboardRepository.getTodaySummary();
        long totalCount = 0;
        long newCount = 0;
        long approvalCount = 0L;
        long rejectedCount = 0L;
        long amendmentCount = 0L;
        long approvedCount = 0L;
        long cancelCount = 0L;
        for (int i = 0; i < pieData.size(); i++) {
            totalCount += pieData.get(i).getCount();
            if (pieData.get(i).getStatus() == 1) newCount = pieData.get(i).getCount();
            if (pieData.get(i).getStatus() == 2) approvalCount = pieData.get(i).getCount();
            if (pieData.get(i).getStatus() == 3) cancelCount = pieData.get(i).getCount();
            if (pieData.get(i).getStatus() == 4) amendmentCount = pieData.get(i).getCount();
            if (pieData.get(i).getStatus() == 5) approvedCount = pieData.get(i).getCount();
            if (pieData.get(i).getStatus() == 6) rejectedCount = pieData.get(i).getCount();
        }
        pieHeaderDataDto
            .get()
            .getData()
            .add(
                new PieDataDto(
                    "New - " + newCount + (newCount > 1 ? " Records" : " Record"),
                    totalCount > 0 ? (float) (newCount * 100) / totalCount : 0
                )
            );
        pieHeaderDataDto
            .get()
            .getData()
            .add(
                new PieDataDto(
                    "Sent for Approval - " + approvalCount + (approvalCount > 1 ? " Records" : " Record"),
                    totalCount > 0 ? (float) (approvalCount * 100) / totalCount : 0
                )
            );

        pieHeaderDataDto
            .get()
            .getData()
            .add(
                new PieDataDto(
                    "Sent for Amendment - " + amendmentCount + (amendmentCount > 1 ? " Records" : " Record"),
                    totalCount > 0 ? (float) (amendmentCount * 100) / totalCount : 0
                )
            );
        pieHeaderDataDto
            .get()
            .getData()
            .add(
                new PieDataDto(
                    "Canceled - " + cancelCount + (cancelCount > 1 ? " Records" : " Record"),
                    totalCount > 0 ? (float) (cancelCount * 100) / totalCount : 0
                )
            );

        pieHeaderDataDto
            .get()
            .getData()
            .add(
                new PieDataDto(
                    "Approved - " + approvedCount + (approvedCount > 1 ? " Records" : " Record"),
                    totalCount > 0 ? (float) (approvedCount * 100) / totalCount : 0
                )
            );

        pieHeaderDataDto
            .get()
            .getData()
            .add(
                new PieDataDto(
                    "Rejected - " + rejectedCount + (rejectedCount > 1 ? " Records" : " Record"),
                    totalCount > 0 ? (float) (rejectedCount * 100) / totalCount : 0
                )
            );

        pieHeaderDataDto.get().setTotalCount(totalCount);

        return pieHeaderDataDto;
    }

    @Override
    public List<HashMap<String, Object>> getDataByTemplate(@Valid InputParamDto param) {
        List<HashMap<String, Object>> list = new ArrayList<>();
        HashMap<String, Object> data = new HashMap<String, Object>();
        List<BasicLineDto> newtList = new ArrayList<>();
        List<BasicLineDto> amendmentList = new ArrayList<>();
        List<BasicLineDto> rejectedList = new ArrayList<>();
        List<BasicLineDto> approvalList = new ArrayList<>();
        List<BasicLineDto> approvedList = new ArrayList<>();
        List<BasicLineDto> canceledList = new ArrayList<>();

        List<LineDataDto> dataDto = dashboardRepository.getDataByTemplate(param);

        for (LineDataDto obj : dataDto) {
            BasicLineDto newCount = new BasicLineDto();
            BasicLineDto amendmentCount = new BasicLineDto();
            BasicLineDto rejectedCount = new BasicLineDto();
            BasicLineDto approvalCount = new BasicLineDto();
            BasicLineDto approvedCount = new BasicLineDto();
            BasicLineDto canceledCount = new BasicLineDto();

            newCount.setName(obj.getDate());
            amendmentCount.setName(obj.getDate());
            rejectedCount.setName(obj.getDate());
            approvalCount.setName(obj.getDate());
            approvedCount.setName(obj.getDate());
            canceledCount.setName(obj.getDate());

            newCount.setCount(obj.getNewCount());
            amendmentCount.setCount(obj.getAmendmentCount());
            rejectedCount.setCount(obj.getRejectedCount());
            approvalCount.setCount(obj.getApprovalCount());
            approvedCount.setCount(obj.getApprovedCount());
            canceledCount.setCount(obj.getCanceledCount());

            newtList.add(newCount);
            amendmentList.add(amendmentCount);
            rejectedList.add(rejectedCount);
            approvalList.add(approvalCount);
            approvedList.add(approvedCount);
            canceledList.add(canceledCount);
        }

        data = new HashMap<String, Object>();
        data.put("type", "New");
        data.put("detail", newtList);
        list.add(data);

        data = new HashMap<String, Object>();
        data.put("type", "Sent for Amendment");
        data.put("detail", amendmentList);
        list.add(data);

        data = new HashMap<String, Object>();
        data.put("type", "Rejected");
        data.put("detail", rejectedList);
        list.add(data);

        data = new HashMap<String, Object>();
        data.put("type", "Sent for Approval");
        data.put("detail", approvalList);
        list.add(data);

        data = new HashMap<String, Object>();
        data.put("type", "Approved");
        data.put("detail", approvedList);
        list.add(data);

        data = new HashMap<String, Object>();
        data.put("type", "Canceled");
        data.put("detail", canceledList);
        list.add(data);

        return list;
    }

    @Override
    public Optional<PieHeaderDataDto> getTodaySummaryByTemplate(@Valid InputParamDto param) {
        Optional<PieHeaderDataDto> pieHeaderDataDto = Optional.of(new PieHeaderDataDto());
        List<PieData> pieData = dashboardRepository.getTodaySummaryByTemplate(param.getTemplateId());
        long totalCount = 0;
        long newCount = 0;
        long approvalCount = 0L;
        long rejectedCount = 0L;
        long amendmentCount = 0L;
        long approvedCount = 0L;
        long cancelCount = 0L;
        for (int i = 0; i < pieData.size(); i++) {
            totalCount += pieData.get(i).getCount();
            if (pieData.get(i).getStatus() == 1) newCount = pieData.get(i).getCount();
            if (pieData.get(i).getStatus() == 2) approvalCount = pieData.get(i).getCount();
            if (pieData.get(i).getStatus() == 3) cancelCount = pieData.get(i).getCount();
            if (pieData.get(i).getStatus() == 4) amendmentCount = pieData.get(i).getCount();
            if (pieData.get(i).getStatus() == 5) approvedCount = pieData.get(i).getCount();
            if (pieData.get(i).getStatus() == 6) rejectedCount = pieData.get(i).getCount();
        }
        pieHeaderDataDto
            .get()
            .getData()
            .add(
                new PieDataDto(
                    "New - " + newCount + (newCount > 1 ? " Records" : " Record"),
                    totalCount > 0 ? (float) (newCount * 100) / totalCount : 0
                )
            );
        pieHeaderDataDto
            .get()
            .getData()
            .add(
                new PieDataDto(
                    "Sent for Approval - " + approvalCount + (approvalCount > 1 ? " Records" : " Record"),
                    totalCount > 0 ? (float) (approvalCount * 100) / totalCount : 0
                )
            );

        pieHeaderDataDto
            .get()
            .getData()
            .add(
                new PieDataDto(
                    "Sent for Amendment - " + amendmentCount + (amendmentCount > 1 ? " Records" : " Record"),
                    totalCount > 0 ? (float) (amendmentCount * 100) / totalCount : 0
                )
            );
        pieHeaderDataDto
            .get()
            .getData()
            .add(
                new PieDataDto(
                    "Canceled - " + cancelCount + (cancelCount > 1 ? " Records" : " Record"),
                    totalCount > 0 ? (float) (cancelCount * 100) / totalCount : 0
                )
            );

        pieHeaderDataDto
            .get()
            .getData()
            .add(
                new PieDataDto(
                    "Approved - " + approvedCount + (approvedCount > 1 ? " Records" : " Record"),
                    totalCount > 0 ? (float) (approvedCount * 100) / totalCount : 0
                )
            );

        pieHeaderDataDto
            .get()
            .getData()
            .add(
                new PieDataDto(
                    "Rejected - " + rejectedCount + (rejectedCount > 1 ? " Records" : " Record"),
                    totalCount > 0 ? (float) (rejectedCount * 100) / totalCount : 0
                )
            );

        pieHeaderDataDto.get().setTotalCount(totalCount);

        return pieHeaderDataDto;
    }

    @Override
    public List<HashMap<String, Object>> getDataByTemplateType(@Valid InputParamDto param) {
        List<HashMap<String, Object>> list = new ArrayList<>();

        Optional<MetaDataDTO> metaDataDto = metaDataRepository
            .findByHeaderIdAndFieldTypeAndShowDashboard(param.getTemplateId(), "LOV", "Y")
            .map(metaDataMapper::toDto);

        if (metaDataDto.isPresent()) {
            String fieldValues[] = metaDataDto.get().getFieldValue().split("\\|");
            List<BarDataDto> dataList = dashboardRepository.getOverAllDataByTemplateAndType(
                param.getTemplateId(),
                metaDataDto.get().getFieldOrder()
            );
            HashMap<String, Object> data = new HashMap<String, Object>();
            for (String value : fieldValues) {
                data = new HashMap<String, Object>();
                BasicLineDto obj = new BasicLineDto();
                obj.setName(value);
                obj.setCount(0L);
                for (BarDataDto barData : dataList) {
                    if (barData.getName().equals(value)) {
                        obj.setCount(barData.getCount());
                        break;
                    }
                }
                data.put("type", value);
                data.put("detail", obj);
                list.add(data);
            }
        }
        return list;
    }

    @Override
    public Optional<PieHeaderDataDto> getOverallSummaryByTemplate() {
        Optional<PieHeaderDataDto> pieHeaderDataDto = Optional.of(new PieHeaderDataDto());
        List<PieData2> pieData = dashboardRepository.getOverallSummaryByTemplate();
        long totalCount = 0;
        for (int i = 0; i < pieData.size(); i++) {
            totalCount += pieData.get(i).getCount();
        }

        for (int i = 0; i < pieData.size(); i++) {
            long count = pieData.get(i).getCount();
            pieHeaderDataDto
                .get()
                .getData()
                .add(
                    new PieDataDto(
                        pieData.get(i).getName() + " - " + count + (count > 1 ? " Records" : " Record"),
                        totalCount > 0 ? (float) (count * 100) / totalCount : 0
                    )
                );
        }

        pieHeaderDataDto.get().setTotalCount(totalCount);

        return pieHeaderDataDto;
    }
}
