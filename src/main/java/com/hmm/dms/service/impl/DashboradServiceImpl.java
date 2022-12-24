package com.hmm.dms.service.impl;

import com.hmm.dms.domain.PieData;
import com.hmm.dms.repository.DashboardRepository;
import com.hmm.dms.repository.DashboardTemplateRepository;
import com.hmm.dms.service.DashboardService;
import com.hmm.dms.service.dto.DashboardTemplateDto;
import com.hmm.dms.service.dto.PieDataDto;
import com.hmm.dms.service.dto.PieHeaderDataDto;
import com.hmm.dms.service.mapper.DashboardTemplateMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
        log.debug("getAllSummary >>>>>>>>>>>>>>>>> " + pieData.size() + pieData.get(0).getStatus());
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
        log.debug("getAllSummary 2 count>>>>>>>>>>>>>>>>> " + (float) (8 * 100) / totalCount);
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
        log.debug("getTodaySummary >>>>>>>>>>>>>>>>> " + pieData.size() + pieData.get(0).getStatus());
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
        log.debug("getAllSummary 2 count>>>>>>>>>>>>>>>>> " + (float) (8 * 100) / totalCount);
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
}
