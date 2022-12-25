package com.hmm.dms.service.impl;

import com.hmm.dms.domain.PieData;
import com.hmm.dms.repository.DashboardRepository;
import com.hmm.dms.repository.DashboardTemplateRepository;
import com.hmm.dms.service.DashboardService;
import com.hmm.dms.service.dto.BasicLineDto;
import com.hmm.dms.service.dto.DashboardTemplateDto;
import com.hmm.dms.service.dto.InputParamDto;
import com.hmm.dms.service.dto.LineDataDto;
import com.hmm.dms.service.dto.PieDataDto;
import com.hmm.dms.service.dto.PieHeaderDataDto;
import com.hmm.dms.service.mapper.DashboardTemplateMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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

    public DashboradServiceImpl(
        DashboardTemplateRepository dashboardTemplateRepository,
        DashboardRepository dashboardRepository,
        DashboardTemplateMapper dashboardTemplateMapper
    ) {
        this.dashboardTemplateRepository = dashboardTemplateRepository;
        this.dashboardRepository = dashboardRepository;
        this.dashboardTemplateMapper = dashboardTemplateMapper;
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
        List<PieData> pieData = dashboardRepository.getDocumentSummary();
        long totalCount = 0L;
        long newCount = 0L;
        Long amendmentCount = 0L;
        Long rejectedCount = 0L;
        Long approvalCount = 0L;
        Long approvedCount = 0L;
        Long cancelCount = 0L;

        for (int i = 0; i < pieData.size(); i++) {
            totalCount += pieData.get(i).getCount();
            if (pieData.get(i).getStatus() == 1) newCount = pieData.get(i).getCount();
            if (pieData.get(i).getStatus() == 2) amendmentCount = pieData.get(i).getCount();
            if (pieData.get(i).getStatus() == 3) rejectedCount = pieData.get(i).getCount();
            if (pieData.get(i).getStatus() == 4) approvalCount = pieData.get(i).getCount();
            if (pieData.get(i).getStatus() == 5) approvedCount = pieData.get(i).getCount();
            if (pieData.get(i).getStatus() == 6) cancelCount = pieData.get(i).getCount();
        }
        pieHeaderDataDto
            .get()
            .getData()
            .add(new PieDataDto("New - " + newCount + (newCount > 1 ? " Records" : " Record"), (float) (newCount * 100) / totalCount));
        pieHeaderDataDto
            .get()
            .getData()
            .add(
                new PieDataDto(
                    "Sent for Amendment - " + amendmentCount + (amendmentCount > 1 ? " Records" : " Record"),
                    (float) (amendmentCount * 100) / totalCount
                )
            );
        pieHeaderDataDto
            .get()
            .getData()
            .add(
                new PieDataDto(
                    "Rejected - " + rejectedCount + (rejectedCount > 1 ? " Records" : " Record"),
                    (float) (rejectedCount * 100) / totalCount
                )
            );
        pieHeaderDataDto
            .get()
            .getData()
            .add(
                new PieDataDto(
                    "Sent for Approval - " + approvalCount + (approvalCount > 1 ? " Records" : " Record"),
                    (float) (approvalCount * 100) / totalCount
                )
            );
        pieHeaderDataDto
            .get()
            .getData()
            .add(
                new PieDataDto(
                    "Approved - " + approvedCount + (approvedCount > 1 ? " Records" : " Record"),
                    (float) (approvedCount * 100) / totalCount
                )
            );
        pieHeaderDataDto
            .get()
            .getData()
            .add(
                new PieDataDto(
                    "Canceled - " + cancelCount + (cancelCount > 1 ? " Records" : " Record"),
                    (float) (cancelCount * 100) / totalCount
                )
            );

        pieHeaderDataDto.get().setTotalCount(totalCount);

        return pieHeaderDataDto;
    }

    @Override
    public Optional<PieHeaderDataDto> getTodaySummary() {
        Optional<PieHeaderDataDto> pieHeaderDataDto = Optional.of(new PieHeaderDataDto());
        List<PieData> pieData = dashboardRepository.getTodaySummary();
        long totalCount = 0L;
        long newCount = 0L;
        Long amendmentCount = 0L;
        Long rejectedCount = 0L;
        Long approvalCount = 0L;
        Long approvedCount = 0L;
        Long cancelCount = 0L;

        for (int i = 0; i < pieData.size(); i++) {
            totalCount += pieData.get(i).getCount();
            if (pieData.get(i).getStatus() == 1) newCount = pieData.get(i).getCount();
            if (pieData.get(i).getStatus() == 2) amendmentCount = pieData.get(i).getCount();
            if (pieData.get(i).getStatus() == 3) rejectedCount = pieData.get(i).getCount();
            if (pieData.get(i).getStatus() == 4) approvalCount = pieData.get(i).getCount();
            if (pieData.get(i).getStatus() == 5) approvedCount = pieData.get(i).getCount();
            if (pieData.get(i).getStatus() == 6) cancelCount = pieData.get(i).getCount();
        }
        pieHeaderDataDto
            .get()
            .getData()
            .add(new PieDataDto("New - " + newCount + (newCount > 1 ? " Records" : " Record"), (float) (newCount * 100) / totalCount));
        pieHeaderDataDto
            .get()
            .getData()
            .add(
                new PieDataDto(
                    "Sent for Amendment - " + amendmentCount + (amendmentCount > 1 ? " Records" : " Record"),
                    (float) (amendmentCount * 100) / totalCount
                )
            );
        pieHeaderDataDto
            .get()
            .getData()
            .add(
                new PieDataDto(
                    "Rejected - " + rejectedCount + (rejectedCount > 1 ? " Records" : " Record"),
                    (float) (rejectedCount * 100) / totalCount
                )
            );
        pieHeaderDataDto
            .get()
            .getData()
            .add(
                new PieDataDto(
                    "Sent for Approval - " + approvalCount + (approvalCount > 1 ? " Records" : " Record"),
                    (float) (approvalCount * 100) / totalCount
                )
            );
        pieHeaderDataDto
            .get()
            .getData()
            .add(
                new PieDataDto(
                    "Approved - " + approvedCount + (approvedCount > 1 ? " Records" : " Record"),
                    (float) (approvedCount * 100) / totalCount
                )
            );
        pieHeaderDataDto
            .get()
            .getData()
            .add(
                new PieDataDto(
                    "Canceled - " + cancelCount + (cancelCount > 1 ? " Records" : " Record"),
                    (float) (cancelCount * 100) / totalCount
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
}
