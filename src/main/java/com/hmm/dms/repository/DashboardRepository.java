package com.hmm.dms.repository;

import com.hmm.dms.domain.PieData;
import com.hmm.dms.domain.PieData2;
import com.hmm.dms.service.dto.BarDataDto;
import com.hmm.dms.service.dto.InputParamDto;
import com.hmm.dms.service.dto.LineDataDto;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unchecked")
@Repository
public class DashboardRepository {

    @Autowired
    EntityManager entityManager;

    public List<PieData> getAllSummary() {
        Query query = entityManager.createNativeQuery(
            "select  count(id) as count, status " + " from document_header " + " where del_flag = 'N' group by status order by status"
        );
        List<Object[]> objList = query.getResultList();
        List<PieData> retList = objList.stream().map(PieData::toDTO).collect(Collectors.toList());
        return retList;
    }

    public List<PieData> getTodaySummary() {
        Query query = entityManager.createNativeQuery(
            " select  count(id) as count, status " +
            " from document_header " +
            " where del_flag = 'N' and created_date > DATE_FORMAT(SYSDATE(), \"%Y-%m-%d\") " +
            " group by status order by status"
        );
        List<Object[]> objList = query.getResultList();
        List<PieData> retList = objList.stream().map(PieData::toDTO).collect(Collectors.toList());
        return retList;
    }

    public List<LineDataDto> getDataByTemplate(@Valid InputParamDto param) {
        Query query = entityManager.createNativeQuery(
            "SELECT  DATE_FORMAT(created_date, \"%Y-%m-%d\") as created_date, " +
            "	sum((case when status = 1 then 1 else 0 end ))  as new_count," +
            "    sum((case when status = 2 then 1 else 0 end ))  as approval_count," +
            "    sum((case when status = 3 then 1 else 0 end ))  as rejected_count," +
            "    sum((case when status = 4 then 1 else 0 end ))  as amendment_count," +
            "    sum((case when status = 5 then 1 else 0 end ))  as approved_count," +
            "    sum((case when status = 6 then 1 else 0 end ))  as canceled_count" +
            " FROM dms.document_header" +
            " group by  DATE_FORMAT(created_date, \"%Y-%m-%d\") "
        );
        List<Object[]> objList = query.getResultList();
        List<LineDataDto> retList = objList.stream().map(LineDataDto::toDTO).collect(Collectors.toList());
        return retList;
    }

    public List<PieData> getTodaySummaryByTemplate(Long templateId) {
        String cri = " ";
        if (templateId != null && templateId.longValue() != 0) cri = " and meta_data_header_id = " + templateId;

        Query query = entityManager.createNativeQuery(
            " select  count(id) as count, status " +
            " from document_header " +
            " where del_flag = 'N' and created_date > DATE_FORMAT(SYSDATE(), \"%Y-%m-%d\") " +
            cri +
            " group by status order by status"
        );
        List<Object[]> objList = query.getResultList();
        List<PieData> retList = objList.stream().map(PieData::toDTO).collect(Collectors.toList());
        return retList;
    }

    public List<BarDataDto> getOverAllDataByTemplateAndType(Long templateId, Integer fieldOrder) {
        String cri = " ";
        if (templateId != null && templateId.longValue() != 0) cri = " and meta_data_header_id = " + templateId;

        Query query = entityManager.createNativeQuery(
            " select count(id) as count, SUBSTRING_INDEX(SUBSTRING_INDEX(field_values,'|'," +
            fieldOrder +
            "),'|',-1) as name" +
            " from dms.document_header " +
            " where del_flag = 'N' " +
            cri +
            " group by SUBSTRING_INDEX(SUBSTRING_INDEX(field_values,'|'," +
            fieldOrder +
            "),'|',-1)"
        );
        List<Object[]> objList = query.getResultList();
        List<BarDataDto> retList = objList.stream().map(BarDataDto::toDTO).collect(Collectors.toList());
        return retList;
    }

    public List<PieData2> getOverallSummaryByTemplate() {
        /*
		  Query query = entityManager.createNativeQuery(
		  "  select  count(a.id) as count, b.doc_title as name " +
		  " from document_header a,  meta_data_header b " +
		  " where a.meta_data_header_id = b.id and a.del_flag = 'N'" +
		  " group by b.doc_title order by b.doc_title"
		  );
		 */

        Query query = entityManager.createNativeQuery(
            "" +
            " select sum((case when doc_id is null then 0 else 1 end )) as count, name " +
            " from (" +
            " select a.id, a.doc_title as name, b.id as doc_id" +
            " from (select id, doc_title from meta_data_header where del_flag = 'N') a" +
            " left join (select id,meta_data_header_id from document_header where del_flag = 'N') b " +
            " on a.id = b.meta_data_header_id " +
            " ) c  group by id, name"
        );

        List<Object[]> objList = query.getResultList();
        List<PieData2> retList = objList.stream().map(PieData2::toDTO).collect(Collectors.toList());
        return retList;
    }
}
