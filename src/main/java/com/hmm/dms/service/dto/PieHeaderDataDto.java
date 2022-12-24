package com.hmm.dms.service.dto;

import java.util.ArrayList;
import java.util.List;

public class PieHeaderDataDto {

    private Long totalCount;
    private List<PieDataDto> data = new ArrayList<PieDataDto>();

    public Long getTotalCount() {
        return totalCount;
    }

    public List<PieDataDto> getData() {
        return data;
    }

    public void setData(List<PieDataDto> data) {
        this.data = data;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    @Override
    public String toString() {
        return "PieHeaderDataDto [totalCount=" + totalCount + ", data=" + data + "]";
    }
}
